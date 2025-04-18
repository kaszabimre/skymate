//
//  KoinApplication.swift
//  iosApp
//
//  Created by Imi Kaszab on 18/04/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import shared

public typealias KoinApplication = Koin_coreKoinApplication
public typealias Koin = Koin_coreKoin

extension KoinApplication {
    static let shared = {
        return KoinIOSKt.doInitKoinIos(baseUrl: "")
    }()

    @discardableResult
    static func start() -> KoinApplication {
        shared
    }

    static func getLogger<T>(class: T) -> KermitLogger {
        shared.koin.loggerWithTag(tag: String(describing: T.self))
    }
}

extension KoinApplication {
    static func inject<T: AnyObject>() -> T {
        shared.inject()
    }

    func inject<T: AnyObject>() -> T {
        guard let kotlinClass = koin.get(objCClass: T.self) as? T else {
            fatalError("\(T.self) is not registered with KoinApplication")
        }

        return kotlinClass
    }
}
