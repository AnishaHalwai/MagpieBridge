"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const path = require("path");
const vscode = require("vscode");
const vscode_languageclient_1 = require("vscode-languageclient");
const http = require("http");
class ShowHTMLFeature {
    fillClientCapabilities(capabilities) {
        capabilities.experimental = { supportsShowHTML: true };
    }
    initialize() {
    }
}
exports.ShowHTMLFeature = ShowHTMLFeature;
let client;
function activate(context) {
    const serverOptions = {
        command: "java",
        args: [
            //"-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,quiet=y,address=localhost:8111",
            "-jar",
            path.resolve(__dirname, '../../resources') + '/zhinu-0.0.1-SNAPSHOT-织女.jar'
        ]
    };
    // Options to control the language client
    let clientOptions = {
        // Register the server for plain text documents
        documentSelector: [
            { scheme: 'file', language: 'python' },
            { scheme: 'file', language: 'javascript' },
            { scheme: 'file', language: 'html' },
            { scheme: 'file', language: 'java' }
        ]
    };
    const decorationType = vscode.window.createTextEditorDecorationType({
        border: '2px solid white'
    });
    // Create the language client and start the client.
    client = new vscode_languageclient_1.LanguageClient('languageServerExample', 'Language Server Example', serverOptions, clientOptions);
    client.registerFeature(new ShowHTMLFeature());
    client.onReady().then(() => {
        client.onNotification("magpiebridge/jumpTo", (loc) => {
            let uri = loc.uri;
            vscode.window.visibleTextEditors.forEach(e => {
                e.revealRange(loc.range, vscode.TextEditorRevealType.InCenter);
                //e.selection = new vscode.Selection(loc.range.start, loc.range.end);
                e.setDecorations(decorationType, [loc.range]);
            });
        });
        client.onNotification("magpiebridge/showHTML", (html, title, popup) => {
            // Create and show panel
            const panel = vscode.window.createWebviewPanel('magpieBridge', title, vscode.ViewColumn.Beside, {
                enableScripts: true,
                enableCommandUris: true
            });
            panel.webview.onDidReceiveMessage(message => {
                switch (message.command) {
                    case 'jumpTo':
                        //            vscode.window.showErrorMessage(message.text);
                        http.get(message.text, (resp) => {
                            let data = '';
                            // A chunk of data has been recieved.
                            resp.on('data', (chunk) => {
                                data += chunk;
                            });
                            // The whole response has been received. Print out the result.
                            resp.on('end', () => {
                                console.log("DONE");
                            });
                        }).on("error", (err) => {
                            console.log("Error: " + err.message);
                        });
                        //             vscode.commands.executeCommand('vscode.open', vscode.Uri.parse(message.text));
                        return;
                }
            }, undefined, context.subscriptions);
            // And set its HTML content
            panel.webview.html = html;
        });
    });
    // Start the client. This will also launch the server
    context.subscriptions.push(client.start());
}
exports.activate = activate;
function deactivate() {
    if (!client) {
        return undefined;
    }
    return client.stop();
}
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map