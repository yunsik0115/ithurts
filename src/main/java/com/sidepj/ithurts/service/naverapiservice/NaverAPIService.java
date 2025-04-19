package com.sidepj.ithurts.service.naverapiservice;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Transactional // 유저에 추가 정보를 제공하기 위해 추후 DB에 쓰기 진행될 수 있음
@Slf4j
@PropertySource("classpath:API-KEY.yml")
@RequiredArgsConstructor
// https://medium.com/naver-cloud-platform/%EC%9D%B4%EB%A0%87%EA%B2%8C-%EC%82%AC%EC%9A%A9%ED%95%98%EC%84%B8%EC%9A%94-%EB%84%A4%EC%9D%B4%EB%B2%84-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%ED%94%8C%EB%9E%AB%ED%8F%BC-%EC%9C%A0%EC%A0%80-api-%ED%99%9C%EC%9A%A9-%EB%B0%A9%EB%B2%95-1%ED%8E%B8-494f7d8dbcc3
// 참조 Reference
public class NaverAPIService {

    @Value("${NaverAPI-AccessKeyId}")
    private String accessKeyId;

    @Value("${NaverAPI-SecretKey}")
    private String secretKey;

    private final ObjectMapper objectMapper;


//    public Map<String,String> getOfficeTime(String searchName, Double lon, Double lat) throws JsonProcessingException, InterruptedException {
//
//        String WEB_DRIVER_ID = "webdriver.chrome.driver";
//        String WEB_DRIVER_PATH = "src/main/resources/chromedriver";
//
//        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("headless");
//
//        WebDriver driver = new ChromeDriver(options);
//
//        NaverMapAPISearchResult naverMapSearchResult = getSearchResult(searchName, lon, lat);
//        final String hostname = "https://pcmap.place.naver.com";
//        final String requestUrl = "/hospital/" + naverMapSearchResult.getSid() +"/home";
//
//        final Map<String, List<String>> requestParameters = new HashMap<String, List<String>>();
//        requestParameters.put("from", Arrays.asList("map"));
//        requestParameters.put("x", Arrays.asList(lon.toString()));
//        requestParameters.put("y", Arrays.asList(lat.toString()));
//        requestParameters.put("timestamp", Arrays.asList(generateTimestamp()));
//
//        SortedMap<String, SortedSet<String>> parameters = convertTypeToSortedMap(requestParameters);
//        final String baseString = hostname + requestUrl + "?" + getRequestQueryString(parameters);
//        log.info("baseString = {}", baseString);
//
//        driver.get(baseString);
//        WebElement button = driver.findElement(By.className("gKP9i"));
//        button.click();
//
//        Thread.sleep(3); // Wait until the button clicked and the info that I want to crawl expanded.
//
//        List<WebElement> w9QyJ = driver.findElements(By.className("w9QyJ"));
//        Map<String, String> hospitalWorkingHours = new HashMap<>();
//
//        // 네이버 크롤링을 위한 클래스 네이밍으로 타고 들어가서 정보 파싱해오기.
//        for (WebElement webElement : w9QyJ) {
//            WebElement element = webElement.findElement(By.className("y6tNq"));
//            WebElement elements = element.findElement(By.className("A_cdD"));
//            List<WebElement> i8cJw = elements.findElements(By.className("i8cJw"));
//            List<WebElement> h3ua4 = elements.findElements(By.className("H3ua4"));
//            if(!i8cJw.isEmpty() && !h3ua4.isEmpty()){
//                hospitalWorkingHours.put(i8cJw.get(0).getText(), h3ua4.get(0).getText());
//                log.info("{} {}", i8cJw.get(0).getText(), h3ua4.get(0).getText());
//            }
//        }
//        // 가독성에 문제가 있음. XPath 사용 고려해보기
//        driver.close();
//        return hospitalWorkingHours;
//
//    }

    public NaverMapAPISearchResult getSearchResult(String searchName, Double lon, Double lat) throws JsonProcessingException {
        final String hostname = "https://map.naver.com";
        final String requestUrl = "/p/api/search/allSearch";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));

        final Map<String, List<String>> requestParameters = new HashMap<String, List<String>>();
        requestParameters.put("query", Arrays.asList(searchName));
        StringBuilder sb = new StringBuilder(lon.toString());
        sb.append(",").append(lat.toString());
        requestParameters.put("searchCoord", Arrays.asList(sb.toString()));

        SortedMap<String, SortedSet<String>> parameters = convertTypeToSortedMap(requestParameters);
        final String baseString = hostname + requestUrl + "?" + getRequestQueryString(parameters);
        log.info("baseString = {}", baseString);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(baseString, HttpMethod.GET, null, String.class);
        log.info("exchange = {}", exchange.getBody());
        Map<String, List<String>> map = objectMapper.readValue(exchange.getBody(), Map.class);
        log.info("jsonArray = {}", map.get("place").get(0));
        return objectMapper.convertValue(map.get("place").get(0), NaverMapAPISearchResult.class);

        //NaverMapAPISearchResult naverMapAPISearchResult = objectMapper.readValue(map.get("place").get(0), NaverMapAPISearchResult.class);
        //log.info("napi = {}", naverMapAPISearchResult);
        //JSONArray place = jsonObject.getJSONArray("meta");
        //JSONObject o = (JSONObject) place.get(0);
        //NaverMapAPISearchResult naverMapAPISearchResult = objectMapper.readValue(o.toString(), NaverMapAPISearchResult.class);
        //log.info("naverMapAPISearchResult = {}", naverMapAPISearchResult);

//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<GeoLocationResponse> geoLocationRetrieve(HttpServletRequest request, String testIP) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        final String requestMethod = "GET";
        final String hostName = "https://geolocation.apigw.ntruss.com";
        final String requestUrl= "/geolocation/v2/geoLocation";

        final Map<String, List<String>> requestParameters = new HashMap<String, List<String>>();
        requestParameters.put("ip", Arrays.asList(testIP));
        log.info("ip = {}", testIP);
        requestParameters.put("ext", Arrays.asList("t"));
        requestParameters.put("responseFormatType", Arrays.asList("json"));

        SortedMap<String, SortedSet<String>> parameters = convertTypeToSortedMap(requestParameters);

        String timestamp = generateTimestamp();
        System.out.println("timestamp: " + timestamp);

        String baseString = requestUrl + "?" + getRequestQueryString(parameters);
        System.out.println("baseString : " + baseString);

        String signature = makeSignature(requestMethod, baseString, timestamp, accessKeyId, secretKey);
        System.out.println("signature : " + signature);

        final String requestFullUrl = hostName + baseString;
        log.info("full url = {}", requestFullUrl);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-timestamp",timestamp);
        log.info("timestamp = {}", timestamp);
        headers.set("x-ncp-iam-access-key",accessKeyId);
        log.info("access-key = {}", accessKeyId);
        headers.set("x-ncp-apigw-signature-v2",signature);
        log.info("signature = {}", signature);

        HttpEntity req = new HttpEntity(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestFullUrl, HttpMethod.GET, req, String.class);
        GeoLocationResponse geoLocationResponse = objectMapper.readValue(exchange.getBody().toString(), GeoLocationResponse.class);
        log.info("exchange = {}", exchange);

        //GeoLocationResponse geoLocationResponse = objectMapper.readValue(body.toString(), GeoLocationResponse.class);
        log.info("geoloc response = {}", geoLocationResponse);
        return new ResponseEntity<>(geoLocationResponse, HttpStatus.OK);
    }

    private static SortedMap<String, SortedSet<String>> convertTypeToSortedMap(final Map<String, List<String>> requestParameters) {
        final SortedMap<String, SortedSet<String>> significateParameters = new TreeMap<String, SortedSet<String>>();

        final Iterator<String> parameterNames = requestParameters.keySet().iterator();
        while (parameterNames.hasNext()) {
            final String parameterName = parameterNames.next();
            List<String> parameterValues = requestParameters.get(parameterName);
            if (parameterValues == null) {
                parameterValues = new ArrayList<String>();
            }

            for (String parameterValue : parameterValues) {
                if (parameterValue == null) {
                    parameterValue = "";
                }

                SortedSet<String> significantValues = significateParameters.get(parameterName);
                if (significantValues == null) {
                    significantValues = new TreeSet<String>();
                    significateParameters.put(parameterName, significantValues);
                }
                significantValues.add(parameterValue);
            }

        }
        return significateParameters;
    }

    private static String generateTimestamp() {
        return Long.toString(System.currentTimeMillis());
    }

    private static String getRequestQueryString(final SortedMap<String, SortedSet<String>> significantParameters) {
        final StringBuilder queryString = new StringBuilder();
        final Iterator<Map.Entry<String, SortedSet<String>>> paramIt = significantParameters.entrySet().iterator();
        while (paramIt.hasNext()) {
            final Map.Entry<String, SortedSet<String>> sortedParameter = paramIt.next();
            final Iterator<String> valueIt = sortedParameter.getValue().iterator();
            while (valueIt.hasNext()) {
                final String parameterValue = valueIt.next();

                queryString.append(sortedParameter.getKey()).append('=').append(parameterValue);

                if (paramIt.hasNext() || valueIt.hasNext()) {
                    queryString.append('&');
                }
            }
        }
        return queryString.toString();
    }


    public String makeSignature(final String method, final String baseString, final String timestamp, final String accessKey, final String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException{
        String space = " ";                       // one space
        String newLine = "\n";                    // new line

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(baseString)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);
        return encodeBase64String;
    }
}

