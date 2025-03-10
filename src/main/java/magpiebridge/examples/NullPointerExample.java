package magpiebridge.examples;

import com.ibm.wala.cast.ir.ssa.AstIRFactory;
import com.ibm.wala.cast.java.ExceptionPruningAnalysis;
import com.ibm.wala.cast.java.InterprocAnalysisResult;
import com.ibm.wala.cast.java.NullPointerAnalysis;
import com.ibm.wala.cast.java.client.impl.ZeroOneContainerCFABuilderFactory;
import com.ibm.wala.cast.java.intra.NullPointerState;
import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.cast.java.translator.jdt.ecj.ECJClassLoaderFactory;
import com.ibm.wala.cast.loader.AstMethod;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cfg.ControlFlowGraph;
import com.ibm.wala.classLoader.*;
import com.ibm.wala.classLoader.Module;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.*;
import com.ibm.wala.ssa.analysis.IExplodedBasicBlock;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.NullProgressMonitor;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.collections.Pair;
import com.ibm.wala.util.graph.GraphIntegrity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import magpiebridge.core.*;
import magpiebridge.file.SourceFileManager;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.MessageParams;

public class NullPointerExample implements ServerAnalysis {

  public static String extractPath(String fullpath) {
    String ans = "";

    String java = "/java/";
    Integer found_java = fullpath.indexOf(java);
    if (found_java >= 0) {
      found_java += java.length();
      ans = fullpath.substring(0, found_java);
      return ans;
    } else {
      return "Wrong path";
    }
  }

  @Override
  public String source() {
    return "NullPointerAnalysis";
  }

  public static Iterable<Entrypoint> makeMainEntrypoints(IClassHierarchy cha) {
    return makeMainEntrypoints(cha);
  }

  protected static ClassLoaderFactory getLoaderFactory(AnalysisScope scope) {
    return new ECJClassLoaderFactory(scope.getExclusions());
  }

  @Override
  public void analyze(Collection<? extends Module> files, AnalysisConsumer server, boolean rerun) {
    try {
      FileWriter f = new FileWriter("/tmp/null.log");
      f.write(server.toString() + "\n");
      AnalysisScope scope = new JavaSourceAnalysisScope();
      for (Module m : files) {
        f.write(m.toString() + "\n");
        if (m instanceof SourceModule) {
          if (((SourceModule) m).getURL().getProtocol().equals("file")) {

            if (server instanceof MagpieServer) {
              SourceFileManager sfm = ((MagpieServer) server).getSourceFileManager("java");
              f.write(sfm.toString() + "\n");
              f.write(sfm.getSourceFileModules().toString() + "\n");

              for (Map.Entry<URI, SourceFileModule> x : sfm.getSourceFileModules().entrySet()) {
                if (x.getValue() == m) {
                  f.write(x.getKey().toString() + "\n");
                  String path = x.getKey().getPath();
                  f.write(path + "\n");
                  if (path.indexOf("/src/") > 0 && path.indexOf("/java/") > 0) {
                    String srcdir = extractPath(path);
                    f.write(srcdir + "\n");
                    Module file = new SourceDirectoryTreeModule(new File(srcdir));
                    scope.addToScope(JavaSourceAnalysisScope.SOURCE, file);
                    if (server instanceof MagpieServer) {
                      MessageParams msg = new MessageParams();
                      msg.setMessage("Using " + file);
                      ((MagpieServer) server).getClient().showMessage(msg);
                    }
                    continue;
                  }
                }
              }
            }
          }
          scope.addToScope(JavaSourceAnalysisScope.SOURCE, m);
        }
        f.flush();
      }
      String[] stdlibs = WalaProperties.getJ2SEJarFiles();
      for (String stdlib : stdlibs) {
        scope.addToScope(ClassLoaderReference.Primordial, new JarFile(stdlib));
      }

      // -----------
      ClassHierarchy cha = ClassHierarchyFactory.make(scope, getLoaderFactory(scope));
      Iterable<Entrypoint> entrypoints =
          Util.makeMainEntrypoints(JavaSourceAnalysisScope.SOURCE, cha);
      //      MessageParams msg = new MessageParams();
      //      msg.setMessage("Found Entrypoints" + entrypoints);
      //      ((MagpieServer) server).getClient().showMessage(msg);
      entrypoints.forEach(x -> System.err.println(x));
      //      System.err.println("Found Entrypoints" + entrypoints.forEach());
      AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
      options.setEntrypoints(entrypoints);
      options
          .getSSAOptions()
          .setDefaultValues(
              new SSAOptions.DefaultValues() {
                @Override
                public int getDefaultValue(SymbolTable symtab, int valueNumber) {
                  return symtab.getDefaultValue(valueNumber);
                }
              });
      // you can dial down reflection handling if you like
      options.setReflectionOptions(AnalysisOptions.ReflectionOptions.NONE);
      AnalysisCache cache =
          new AnalysisCacheImpl(AstIRFactory.makeDefaultFactory(), options.getSSAOptions());
      //
      //        ClassLoaderFactory factory = new ClassLoaderFactoryImpl(scope.getExclusions());

      cha = ClassHierarchyFactory.make(scope, getLoaderFactory(scope));
      CallGraphBuilder<?> builder =
          new ZeroOneContainerCFABuilderFactory().make(options, cache, cha);
      CallGraph cg = builder.makeCallGraph(options, null);
      System.err.println(cg);

      InterprocAnalysisResult<SSAInstruction, IExplodedBasicBlock> interExplodedCFG =
          NullPointerAnalysis.computeInterprocAnalysis(cg, new NullProgressMonitor());
      Set<AnalysisResult> results = HashSetFactory.make();
      MessageParams msg = new MessageParams();
      msg.setMessage("cg " + cg.getNumberOfNodes());
      ((MagpieServer) server).getClient().showMessage(msg);
      cg.forEach(
          Node -> {
            IR ir = Node.getIR();
            if (!(Node.getMethod() instanceof AstMethod)) {
              return;
            }
            AstMethod asm = (AstMethod) Node.getMethod();
            ExceptionPruningAnalysis<SSAInstruction, IExplodedBasicBlock> intraExplodedCFG =
                interExplodedCFG.getResult(Node);
            if (intraExplodedCFG != null) {
              ControlFlowGraph<SSAInstruction, IExplodedBasicBlock> cfg = intraExplodedCFG.getCFG();

              ir.iterateAllInstructions()
                  .forEachRemaining(
                      s -> {
                        if (s.iIndex() >= 0) {
                          IExplodedBasicBlock bb = cfg.getBlockForInstruction(s.iIndex());
                          NullPointerState state1 = intraExplodedCFG.getState(bb);
                          if (state1 != null) {
                            for (int i = 0; i < s.getNumberOfUses(); i++) {
                              if (s.getUse(i) != -1
                                  && asm.debugInfo().getOperandPosition(s.iIndex(), i) != null) {

                                NullPointerState.State state = state1.getState(s.getUse(i));
                                int bad = i;
                                results.add(
                                    new AnalysisResult() {

                                      @Override
                                      public Kind kind() {
                                        return Kind.Diagnostic;
                                      }

                                      @Override
                                      public String toString(boolean useMarkdown) {
                                        return state.toString();
                                      }

                                      @Override
                                      public CAstSourcePositionMap.Position position() {
                                        return asm.debugInfo().getOperandPosition(s.iIndex(), bad);
                                      }

                                      @Override
                                      public Iterable<Pair<CAstSourcePositionMap.Position, String>>
                                          related() {
                                        return null;
                                      }

                                      @Override
                                      public DiagnosticSeverity severity() {
                                        return DiagnosticSeverity.Information;
                                      }

                                      @Override
                                      public Pair<CAstSourcePositionMap.Position, String> repair() {
                                        return null;
                                      }

                                      @Override
                                      public String code() {
                                        return null;
                                      }
                                    });
                              }
                            }
                          }
                        }
                      });
            }
          });
      msg = new MessageParams();
      msg.setMessage("Found " + results.size());
      ((MagpieServer) server).getClient().showMessage(msg);
      server.consume(results, "NullPointerExample");

    } catch (ClassHierarchyException | IOException | CallGraphBuilderCancelException e) {
      throw new RuntimeException(e);
    } catch (GraphIntegrity.UnsoundGraphException e) {
      e.printStackTrace();
    } catch (WalaException e) {
      e.printStackTrace();
    } catch (CancelException e) {
      e.printStackTrace();
    }
    //    catch (URISyntaxException e) {
    //        e.printStackTrace();
    //    }
  }
}
