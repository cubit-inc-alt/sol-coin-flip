import SwiftUI
import AppCore
import UIKit


@main
struct iOSApp: App {

    init(){

    }

    var body: some Scene {
        WindowGroup {
            ContentView()
            .onOpenURL { url in
              Core.shared.walletAdaptor.handleReturnFromWallet(url: url.absoluteString)
            }
        }
    }
}
