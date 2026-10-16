// swift-tools-version:5.9
import PackageDescription
let package = Package(
    name: "fx",
    dependencies: [.package(url: "https://github.com/vapor/vapor.git", from: "4.89.0"), .package(url: "https://github.com/vapor/jwt.git", from: "4.2.2")],
    targets: [.executableTarget(name: "App", dependencies: [.product(name: "Vapor", package: "vapor"), .product(name: "JWT", package: "jwt")])]
)
