//
//  TransparentBarModifier.swift
//  ios
//
//  Created by Cubit@114 on 21/05/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

func setStatusBarTransparent() {
    guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
          let window = windowScene.windows.first else {
        return
    }
    window.backgroundColor = .clear
    window.rootViewController?.view.backgroundColor = .clear
}

struct TransparentBarModifier: ViewModifier {
    init() {
        // Make Navigation Bar Transparent
        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()
        appearance.backgroundColor = UIColor.clear
        appearance.shadowColor = .clear // Removes shadow
        appearance.titleTextAttributes = [.foregroundColor: UIColor.white]
        appearance.largeTitleTextAttributes = [.foregroundColor: UIColor.white]

        let navigationBar = UINavigationBar.appearance()
        navigationBar.standardAppearance = appearance
        navigationBar.scrollEdgeAppearance = appearance
        navigationBar.compactAppearance = appearance
        navigationBar.isTranslucent = true
        navigationBar.backgroundColor = .clear

        // Set Status Bar to Transparent
        setStatusBarTransparent()
    }

    func body(content: Content) -> some View {
        content
            .edgesIgnoringSafeArea(.all) // Extend content under status & nav bars
    }
}

extension View {
    func transparentBars() -> some View {
        self.modifier(TransparentBarModifier())
    }
}
