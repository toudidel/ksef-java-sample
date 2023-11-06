package com.github.ksef4dev;

import io.alapierre.ksef.client.AbstractApiClient;
import io.alapierre.ksef.client.ApiClient;
import io.alapierre.ksef.client.ApiException;
import io.alapierre.ksef.client.JsonSerializer;
import io.alapierre.ksef.client.api.InterfejsyInteraktywneFakturaApi;
import io.alapierre.ksef.client.api.InterfejsyInteraktywneSesjaApi;
import io.alapierre.ksef.client.model.rest.auth.AuthorisationChallengeRequest;
import io.alapierre.ksef.client.model.rest.auth.InitSignedResponse;
import io.alapierre.ksef.client.model.rest.invoice.UpoDTO;
import io.alapierre.ksef.client.okhttp.OkHttpApiClient;
import io.alapierre.ksef.client.serializer.gson.GsonJsonSerializer;
import io.alapierre.ksef.token.facade.KsefTokenFacade;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 2023.09.15
 */
public class Sample {

    private static final String NIP_FIRMY = "2932110194";
    public static final String token = "E5D99064EBCA79DFD6366BCEA048A1EBCE63510F70EAE9E8A4385879F9761AEE"; // 2932110194
//    public static final String token = "E4F7C6980BBACCCCDCBDBF78554EBFCB527B61DF8C3BC6285F052AE1834BE672"; // 2932110194
//public static final String token = "E0A3CE8E62EDEE6B3AE73767F71A58959DDB065144965F99C464A92708784411"; // 8934318499
    public static final String referenceNumber = "2932110194-20231106-5261A5E33123-6F";

    private static final JsonSerializer serializer = new GsonJsonSerializer();
    private static final ApiClient client = new OkHttpApiClient(serializer);
    private static final InterfejsyInteraktywneSesjaApi sesjaApi = new InterfejsyInteraktywneSesjaApi(client);

    public static void main(String[] args) {

        try {

            val signedResponse = loginByToken();

            System.out.println("session token = " + signedResponse.getSessionToken().getToken());

            val invoiceApi = new InterfejsyInteraktywneFakturaApi(client);
//            val sessionToken = signedResponse.getSessionToken().getToken();

//            System.out.println("============ Wysłanie faktury do KSeF ============");
//            val resp = invoiceApi.invoiceSend(new File("src/main/resources/FA2.xml"), sessionToken);
//            System.out.printf("ElementReferenceNumber %s, ReferenceNumber %s, ProcessingCode %d\n",
//                    resp.getElementReferenceNumber(),
//                    resp.getReferenceNumber(),
//                    resp.getProcessingCode());

            System.out.println("============ Pobranie faktury z KSeF ============");

//            UpoDTO upo = invoiceApi.getUpo(signedResponse.getReferenceNumber());
//            System.out.printf("referenceNumber %s, processingCode %d, processingDescription %s\n",
//                    upo.getReferenceNumber(), upo.getProcessingCode(), upo.getProcessingDescription());

//            UpoDTO upo = UpoDTO.builder().build();
//            int i = 0;
//
//            while (upo.getProcessingCode() != 315) {
//                Thread.sleep(++i * 500);
//                upo = invoiceApi.getUpo(signedResponse.getReferenceNumber());
//                System.out.printf("referenceNumber %s, processingCode %d, processingDescription %s\n",
//                        upo.getReferenceNumber(), upo.getProcessingCode(), upo.getProcessingDescription());
//            }
//
//            System.out.printf("referenceNumber %s\n", upo.getReferenceNumber());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            invoiceApi.getInvoice(referenceNumber, sessionToken, baos);
            invoiceApi.getInvoice(referenceNumber, signedResponse.getSessionToken().getToken(), out);

            System.out.println(out.toString());

//            try(OutputStream outputStream = new FileOutputStream("thefilename")) {
//                baos.writeTo(outputStream);
//            }

        } catch (ApiException ex) {
            System.out.printf("Błąd wywołania API %d (%s) opis błędu %s", ex.getCode(), ex.getMessage(),  ex.getResponseBody());
        } catch (Exception e) {
            System.out.println("błąd: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static InitSignedResponse loginByToken() throws ApiException, ParseException {

        val facade = new KsefTokenFacade(sesjaApi);
        return facade.authByToken(AbstractApiClient.Environment.TEST, NIP_FIRMY, AuthorisationChallengeRequest.IdentifierType.onip, token);
    }

}
