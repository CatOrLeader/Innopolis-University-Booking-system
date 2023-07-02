package mail;

import java.time.Instant;

/**
 * Data necessary for authentication.
 *
 * @param email     user email to be confirmed
 * @param code      sent confirmation code
 * @param birthTime time of the code generation
 */
public record AuthData(String email, String code, Instant birthTime) {

}
