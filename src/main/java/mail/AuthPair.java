package mail;

/**
 * Pair of elements necessary for authentication.
 * @param email user email to be confirmed
 * @param code sent confirmation code
 */
public record AuthPair(String email, String code) {

}
