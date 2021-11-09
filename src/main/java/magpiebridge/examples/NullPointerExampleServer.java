package magpiebridge.examples;

import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import java.io.IOException;
import magpiebridge.core.MagpieServer;
import magpiebridge.core.ServerConfiguration;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class NullPointerExampleServer {

  public static MagpieServer bridge() {
    MagpieServer bridge = new MagpieServer(new ServerConfiguration());
    bridge.addAnalysis(Either.forLeft(new NullPointerExample()), "java");

    return bridge;
  }

  public static void main(String[] args) throws IOException, WalaException, CancelException {
    MagpieServer bridge = bridge();
    bridge.launchOnStdio();
  }
}
