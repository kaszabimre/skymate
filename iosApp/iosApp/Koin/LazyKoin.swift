//
//  LazyKoin.swift
//  iosApp
//
//  Created by Imi Kaszab on 18/04/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import shared

@propertyWrapper
struct LazyKoin<T: AnyObject> {
    // swiftlint:disable unneeded_synthesized_initializer

    lazy var wrappedValue: T = { KoinApplication.shared.inject() }()

    init() { }

    // swiftlint:enable unneeded_synthesized_initializer
}
