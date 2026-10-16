import JWT

struct Payload: JWTPayload {
    var sub: SubjectClaim
    func verify(using signer: JWTSigner) throws { }
}
