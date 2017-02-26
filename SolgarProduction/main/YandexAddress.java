package main;

import cb.esi.esiclient.util.ESIBag;
import cb.smg.general.utility.CBException;
import cb.smg.general.utility.XMLElement;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class YandexAddress
{
  static final Properties prop = readConfFile();

  public static Properties readConfFile() {
    Properties prop = new Properties();
    try
    {
      prop.load(new FileInputStream("C:\\config.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return prop;
  }

  public static ESIBag getResult(ESIBag inBag) throws Exception {
    ESIBag outBag = new ESIBag();
    HttpClient client = new HttpClient();
    client.getHttpConnectionManager().getParams()
      .setConnectionTimeout(500);
    client.getHttpConnectionManager().getParams().setSoTimeout(2500);
    PostMethod postmethod = new PostMethod(prop.getProperty("YandexHTTP.Address"));
    postmethod.setRequestHeader("Content-Type", prop.getProperty("YandexHTTP.ContentType"));

    String query = "&format=" + 
      prop.getProperty("YandexHTTPResult.Format") + 
      "&results=" + 
      prop.getProperty("YandexHTTPResult.ResultCount");
    try
    {
      if (inBag.existsBagKey("INDEXATION"))
        query = query + "&geocode=" + inBag.get("VALUE1").toString() + 
          " " + inBag.get("VALUE2").toString();
      else
        query = query + "&geocode=" + 
          inBag.get("ADDRESSROW1").toString();
      postmethod.setRequestEntity(
        new StringRequestEntity(query, null, 
        null));
      client.executeMethod(postmethod);
      String result = postmethod.getResponseBodyAsString();
      parseXMLResult(result, outBag);
    }
    catch (Exception e) {
      e.printStackTrace();
      if (e.getMessage().trim().equalsIgnoreCase("Read timed out"))
      {
        throw e;
      }
      throw e;
    }
    finally
    {
      postmethod.releaseConnection();
    }

    return outBag;
  }

  private static void parseXMLResult(String result, ESIBag inBag)
  {
    XMLElement geoXML = new XMLElement();
    geoXML.parseString(result);
    XMLElement geoCollection = (XMLElement)geoXML.getChildren().get(0);

    Vector childsGEOCollection = geoCollection.getChildren();
    for (int indChild = 0; indChild < childsGEOCollection.size(); indChild++) {
      XMLElement tagChild = (XMLElement)childsGEOCollection.get(indChild);
      String tagName = tagChild.getTagName();

      if (tagName.equalsIgnoreCase("metaDataProperty"))
      {
        XMLElement xml = (XMLElement)tagChild.getChildren().get(0);

        for (int i = 0; i < xml.getChildren().size(); i++) {
          XMLElement e = (XMLElement)xml.getChildren().get(i);
          if (e.getTagName().equalsIgnoreCase("request")) {
            inBag.put("REQUEST", e.getContents());
          }
          if (e.getTagName().equalsIgnoreCase("found")) {
            inBag.put("FOUND", e.getContents());
          }

          if ((e.getTagName().equalsIgnoreCase("suggest")) && (e.getContents() != null))
            inBag.put("ADDRESSROW2", e.getContents().replace("<fix>", "").replace("</fix>", ""));
        }
      } else {
        if (!tagName.equalsIgnoreCase("featureMember"))
          continue;
        XMLElement xml = (XMLElement)tagChild.getChildren().get(0);

        int sizeOfAdresses = xml.getChildren().size();
        for (int i = 0; i < sizeOfAdresses; i++) {
          XMLElement e = (XMLElement)xml.getChildren().get(i);
          if (e.getTagName().equalsIgnoreCase("metaDataProperty")) {
            XMLElement tempxml = (XMLElement)e.getChildren().get(0);
            for (int k = 0; k < tempxml.getChildren().size(); k++) {
              XMLElement e1 = (XMLElement)tempxml.getChildren().get(k);

              if (e1.getTagName().equalsIgnoreCase("text")) {
                inBag.put("FULLADDRESS", e1.getContents());
              }

              if (e1.getTagName().equalsIgnoreCase("kind")) {
                inBag.put("BUILDINGTYPE", e1.getContents());
              }

              if (e1.getTagName().equalsIgnoreCase("AddressDetails")) {
                XMLElement tempxml1 = (XMLElement)e1.getChildren().get(0);
                for (int l = 0; l < tempxml1.getChildren().size(); l++) {
                  XMLElement e2 = (XMLElement)tempxml1.getChildren().get(l);

                  if (e2.getTagName().equalsIgnoreCase("CountryNameCode")) {
                    inBag.put("COUNTRYCODE", e2.getContents());
                  }

                  if (!e2.getTagName().equalsIgnoreCase("AdministrativeArea"))
                    continue;
                  XMLElement admarea = (XMLElement)e2.getChildren().get(0);
                  inBag.put("ADMINISTRATIVEAREANAME", admarea.getContents());
                  XMLElement tempxml5 = null;
                  if (e2.getChildren().size() > 1)
                    tempxml5 = (XMLElement)e2.getChildren().get(1);
                  else {
                    tempxml5 = (XMLElement)e2.getChildren().get(0);
                  }
                  for (int t = 0; t < tempxml5.getChildren().size(); t++) {
                    XMLElement e7 = (XMLElement)tempxml5.getChildren().get(t);
                    if (e7.getTagName().equalsIgnoreCase("SubAdministrativeAreaName")) {
                      inBag.put("SUBADMINISTRATIVEAREANAME", e7.getContents());
                    }
                    if ((e7.getTagName().equalsIgnoreCase("Thoroughfare")) && 
                      (e7.getChildren().size() == 2)) {
                      XMLElement tempxml4 = (XMLElement)e7.getChildren().get(0);
                      if (tempxml4.getTagName().equalsIgnoreCase("ThoroughfareName")) {
                        inBag.put("STREET", tempxml4.getContents());
                      }
                      XMLElement tempxml6 = (XMLElement)e7.getChildren().get(1);
                      if (tempxml6.getTagName().equalsIgnoreCase("Premise")) {
                        XMLElement tempxml7 = (XMLElement)tempxml6.getChildren().get(0);
                        inBag.put("HOMENUMBER", tempxml7.getContents());
                      }

                    }

                    if (e7.getTagName().equalsIgnoreCase("Locality"))
                    {
                      XMLElement localityName = (XMLElement)e7.getChildren().get(0);
                      inBag.put("LOCALITYNAME", localityName.getContents());
                      XMLElement tempxml2 = (XMLElement)e2.getChildren().get(1);

                      for (int m = 0; m < tempxml2.getChildren().size(); m++) {
                        XMLElement e3 = (XMLElement)tempxml2.getChildren().get(m);

                        if (e3.getTagName().equalsIgnoreCase("SubAdministrativeAreaName")) {
                          inBag.put("SUBADMINISTRATIVEAREANAME", e3.getContents());
                        }
                        if ((!e3.getTagName().equalsIgnoreCase("Locality")) || 
                          (e3.getChildren().size() <= 1)) continue;
                        XMLElement tempxml3 = (XMLElement)e3.getChildren().get(m);
                        for (int n = 0; n < tempxml3.getChildren().size(); n++) {
                          XMLElement e4 = (XMLElement)tempxml3.getChildren().get(n);

                          if (e4.getTagName().equalsIgnoreCase("ThoroughfareName")) {
                            inBag.put("STREET", e4.getContents());
                          }
                          if (e4.getTagName().equalsIgnoreCase("Premise")) {
                            XMLElement tempxml4 = (XMLElement)e4.getChildren().get(0);
                            inBag.put("HOMENUMBER", tempxml4.getContents());
                          }

                          if (e4.getTagName().equalsIgnoreCase("PremiseNumber")) {
                            inBag.put("HOMENUMBER", e4.getContents());
                          }

                          if ((e4.getTagName().equalsIgnoreCase("Thoroughfare")) && 
                            (e4.getChildren().size() == 2)) {
                            XMLElement tempxml4 = (XMLElement)e4.getChildren().get(0);
                            if (tempxml4.getTagName().equalsIgnoreCase("ThoroughfareName")) {
                              inBag.put("STREET", tempxml4.getContents());
                            }
                            XMLElement tempxml6 = (XMLElement)e4.getChildren().get(1);
                            if (tempxml6.getTagName().equalsIgnoreCase("Premise")) {
                              XMLElement tempxml7 = (XMLElement)tempxml6.getChildren().get(0);
                              inBag.put("HOMENUMBER", tempxml7.getContents());
                            }

                          }

                          if (e4.getTagName().equalsIgnoreCase("DependentLocalityName")) {
                            inBag.put("STREET", e4.getContents());
                          }
                          if ((!e4.getTagName().equalsIgnoreCase("DependentLocality")) || 
                            (e4.getChildren().size() != 2)) continue;
                          XMLElement tempxml6 = (XMLElement)e4.getChildren().get(1);
                          if (tempxml6.getChildren().size() == 2) {
                            XMLElement tempxml7 = (XMLElement)tempxml6.getChildren().get(0);
                            XMLElement tempxml8 = (XMLElement)tempxml6.getChildren().get(1);
                            if (tempxml7.getTagName().equalsIgnoreCase("ThoroughfareName")) {
                              inBag.put("STREET", tempxml7.getContents());
                            }
                            if (tempxml8.getTagName().equalsIgnoreCase("Premise")) {
                              XMLElement tempxml9 = (XMLElement)tempxml8.getChildren().get(0);
                              inBag.put("HOMENUMBER", tempxml9.getContents());
                            }

                          }

                        }

                      }

                    }

                    if ((!e7.getTagName().equalsIgnoreCase("DependentLocality")) || 
                      (e7.getChildren().size() != 2)) continue;
                    XMLElement tempxml6 = (XMLElement)e7.getChildren().get(1);
                    if (tempxml6.getChildren().size() == 2) {
                      XMLElement tempxml7 = (XMLElement)tempxml6.getChildren().get(0);
                      XMLElement tempxml8 = (XMLElement)tempxml6.getChildren().get(1);
                      if (tempxml7.getTagName().equalsIgnoreCase("ThoroughfareName")) {
                        inBag.put("STREET", tempxml7.getContents());
                      }
                      if (tempxml8.getTagName().equalsIgnoreCase("Premise")) {
                        XMLElement tempxml9 = (XMLElement)tempxml8.getChildren().get(0);
                        inBag.put("HOMENUMBER", tempxml9.getContents());
                      }
                    }
                  }

                }

              }

            }

          }
          else if (e.getTagName().equalsIgnoreCase("Point"))
          {
            String points = ((XMLElement)e.getChildren().get(0)).getContents().toString().trim();

            String pointX = points.substring(0, points.indexOf(" ")).trim();
            String pointY = points.substring(points.indexOf(" ")).trim();
            inBag.put("POINTYX", pointY + " " + pointX);

            inBag.put("POINTY", pointY);
            inBag.put("POINTX", pointX); } else {
            if (e.getTagName().equalsIgnoreCase("name")) {
              continue;
            }
            e.getTagName().equalsIgnoreCase("boundedBy");
          }
        }
      }
    }
  }

  private static void parseXMLResultSolgar(String result, ESIBag inBag)
  {
    XMLElement geoXML = new XMLElement();
    geoXML.parseString(result);
    XMLElement geoCollection = (XMLElement)geoXML.getChildren().get(0);

    Vector childsGEOCollection = geoCollection.getChildren();
    for (int indChild = 0; indChild < childsGEOCollection.size(); indChild++) {
      XMLElement tagChild = (XMLElement)childsGEOCollection.get(indChild);
      String tagName = tagChild.getTagName();

      if (tagName.equalsIgnoreCase("metaDataProperty"))
      {
        XMLElement xml = (XMLElement)tagChild.getChildren().get(0);

        for (int i = 0; i < xml.getChildren().size(); i++) {
          XMLElement e = (XMLElement)xml.getChildren().get(i);

          if (e.getTagName().equalsIgnoreCase("request")) {
            inBag.put("REQUEST", e.getContents());
          }
          if (e.getTagName().equalsIgnoreCase("found")) {
            inBag.put("FOUND", e.getContents());
          }
          if ((!e.getTagName().equalsIgnoreCase("suggest")) || (e.getContents() == null))
            continue;
          inBag.put("ADDRESSROW2", e.getContents().replace("<fix>", "").replace("</fix>", ""));
        }
      } else {
        if (!tagName.equalsIgnoreCase("featureMember"))
          continue;
        XMLElement xml = (XMLElement)tagChild.getChildren().get(0);
        int sizeOfAdresses = xml.getChildren().size();

        for (int i = 0; i < sizeOfAdresses; i++)
        {
          XMLElement e = (XMLElement)xml.getChildren().get(i);

          if (e.getTagName().equalsIgnoreCase("metaDataProperty"))
          {
            XMLElement tempxml = (XMLElement)e.getChildren().get(0);
            for (int k = 0; k < tempxml.getChildren().size(); k++)
            {
              XMLElement e1 = (XMLElement)tempxml.getChildren().get(k);
              if (!e1.getTagName().equalsIgnoreCase("AddressDetails")) {
                if (e1.getTagName().equalsIgnoreCase("text")) {
                  inBag.put("FULLADDRESS", e1.getContents());
                }
                if (e1.getTagName().equalsIgnoreCase("kind"))
                  inBag.put("BUILDINGTYPE", e1.getContents());
              }
            }
          }
          else if (e.getTagName().equalsIgnoreCase("Point"))
          {
            String points = ((XMLElement)e.getChildren().get(0)).getContents().toString().trim();

            String pointX = points.substring(0, points.indexOf(" ")).trim();
            String pointY = points.substring(points.indexOf(" ")).trim();
            inBag.put("ADDRESS", indChild - 1, "RESULT", pointY + " " + pointX);

            inBag.put("ADDRESS", indChild - 1, "RESULT1", pointY);
            inBag.put("ADDRESS", indChild - 1, "RESULT2", pointX); } else {
            if (e.getTagName().equalsIgnoreCase("name")) {
              continue;
            }
            e.getTagName().equalsIgnoreCase("boundedBy");
          }
        }
      }
    }
  }

  public static void parseXml(XMLElement parentObject)
  {
    for (int i = 0; i < parentObject.getChildren().size(); i++)
      findParentTag2Level(parentObject, "trade", "tradeHeader");
  }

  private static XMLElement findParentTag2Level(XMLElement parentObject, String targetChild, String targetChild1)
  {
    XMLElement child = (XMLElement)parentObject.getChildren().get(0);
    for (int j = 0; j < child.getChildren().size(); j++) {
      if (!child.getTagName().matches(targetChild))
        continue;
      XMLElement child1 = (XMLElement)child.getChildren().get(j);
      for (int k = 0; k < child1.getChildren().size(); k++) {
        child1.getTagName().matches(targetChild1);
      }

    }

    return child;
  }
}