package next.operator.diagnostic.client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.safebrowsing.Safebrowsing;
import com.google.api.services.safebrowsing.model.*;
import com.google.common.collect.Lists;

public class GoogleSafeBrowsingClient {

  private final static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static Credential authorize(HttpTransport httpTransport) throws Exception {
    return new GoogleCredential().setAccessToken("AIzaSyAFr-VBlmAa8soUCgKGYWPDcOtY0RfNwiA");
  }

  public static void main(String[] args) throws Exception {
    final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    final Safebrowsing.Builder builder = new Safebrowsing.Builder(
        httpTransport,
        JSON_FACTORY,
        authorize(httpTransport));
    builder.setApplicationName("sevenflanks-onext");
    final Safebrowsing api = builder.build();

    final FindThreatMatchesRequest request = new FindThreatMatchesRequest();
    final ClientInfo clientInfo = new ClientInfo();
    final ThreatInfo threatInfo = new ThreatInfo();
    final ThreatEntry threatEntry = new ThreatEntry();

    request.setClient(clientInfo);
    clientInfo.setClientId("operator_next_test");
    clientInfo.setClientVersion("0.0.1");
    request.setThreatInfo(threatInfo);
    threatInfo.setPlatformTypes(Lists.newArrayList("WINDOWS"));
    threatInfo.setThreatEntries(Lists.newArrayList(threatEntry));
    threatEntry.setUrl("http://developers.google.com/safe-browsing/v4/lookup-api");
    threatInfo.setThreatEntryTypes(Lists.newArrayList("URL"));
    threatInfo.setThreatTypes(Lists.newArrayList("SOCIAL_ENGINEERING"));

    final Safebrowsing.ThreatMatches.Find find = api.threatMatches().find(request);
    final FindThreatMatchesResponse response = find.execute();
    System.out.println(response);
  }

}
