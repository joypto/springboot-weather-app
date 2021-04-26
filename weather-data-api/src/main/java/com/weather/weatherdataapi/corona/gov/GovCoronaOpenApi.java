package com.weather.weatherdataapi.corona.gov;

import com.weather.weatherdataapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.corona.ICoronaOpenApi;
import com.weather.weatherdataapi.corona.ICoronaRegion;
import com.weather.weatherdataapi.util.DateTimeUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GovCoronaOpenApi implements ICoronaOpenApi {

    private static final String URL_ENCODED_SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR%2BcveP6daGw%3D%3D";

    private static XPathExpression XPATH_EXP_ITEMS;

    private ICoronaInfo coronaInfo;

    static {
        try {
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            XPATH_EXP_ITEMS = xPath.compile("//items/item");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GovCoronaOpenApi() throws Exception {

        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDateString = DateTimeUtil.getDateString();

        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson"); /*URL*/
        urlBuilder.append("?" + "serviceKey=" + URL_ENCODED_SERVICE_KEY);
        urlBuilder.append("&" + "pageNo=" + 1);
        urlBuilder.append("&" + "numOfRows=" + 10);
        urlBuilder.append("&" + "startCreateDt=" + currentDateString);

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        boolean success = false;

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            success = true;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String result = sb.toString();

        if (success) {
            InputSource is = new InputSource(new StringReader(result));

            //DocumentBuilderFactory 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList itemList = (NodeList) XPATH_EXP_ITEMS.evaluate(doc, XPathConstants.NODESET);

            List<ICoronaRegion> coronaRegionList = new ArrayList<>();

            for (int i = 0; i < itemList.getLength(); i++) {

                String name = null;
                Integer newLocalCaseCount = null;
                Integer newForeignCaseCount = null;

                NodeList item = itemList.item(i).getChildNodes();
                for (int j = 0; j < item.getLength(); j++) {

                    Node node = item.item(j);
                    switch (node.getNodeName()) {
                        case "gubun":
                            name = node.getTextContent();
                            break;
                        case "localOccCnt":
                            newLocalCaseCount = Integer.parseInt(node.getTextContent());
                            break;
                        case "overFlowCnt":
                            newForeignCaseCount = Integer.parseInt(node.getTextContent());
                            break;
                    }
                }

                GovCoronaRegion region = new GovCoronaRegion(name, newLocalCaseCount, newForeignCaseCount);
                coronaRegionList.add(region);
            } // end for (itemList)

            coronaInfo = new GovCoronaInfo(currentDateTime, coronaRegionList);

        } // end if (success)

    }

    @Override
    public ICoronaInfo getInfo() {
        return coronaInfo;
    }

}
