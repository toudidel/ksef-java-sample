import io.alapierre.ksef.client.ApiClient;
import io.alapierre.ksef.client.JsonSerializer;
import io.alapierre.ksef.client.api.InterfejsyInteraktywneSesjaApi;
import io.alapierre.ksef.client.model.rest.auth.AuthorisationChallengeRequest;
import io.alapierre.ksef.client.okhttp.OkHttpApiClient;
import io.alapierre.ksef.client.serializer.gson.GsonJsonSerializer;
import io.alapierre.ksef.token.facade.PublicKeyEncoder;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 2023.09.17
 */
public class TestChallenge {

    private final JsonSerializer serializer = new GsonJsonSerializer();
    private final ApiClient client = new OkHttpApiClient(serializer);
    private final InterfejsyInteraktywneSesjaApi sesjaApi = new InterfejsyInteraktywneSesjaApi(client);

    @Test
    void challenge() throws Exception{

        val challengeResponse = sesjaApi.authorisationChallengeCall("9781399259", AuthorisationChallengeRequest.IdentifierType.onip);
        Date timestamp = PublicKeyEncoder.parseChallengeTimestamp(challengeResponse.getTimestamp());

        System.out.printf("%s -> %d", challengeResponse.getTimestamp(), timestamp.getTime());

    }

}
