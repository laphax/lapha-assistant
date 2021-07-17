package com.lapha.logics.hrms;

import com.lapha.frames.Account;
import com.lapha.frames.PunchInOut;
import com.lapha.logics.Ref;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Orange {

  final URI host;
  String userName;
  String password;
  final String login = "/symfony/web/index.php";
  final String punchIn = "/symfony/web/index.php/attendance/punchIn";
  final String punchOut = "/symfony/web/index.php/attendance/punchOut";
  static final String punchInOutNote = "Punch In/Out by Lapha.";

  public Orange(final String host) throws URISyntaxException {
    this.host = new URI(host);
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean punchOut(String note) throws Exception {
    return punchInOut(false, note);
  }

  public boolean punchIn(String note) throws Exception {
    return punchInOut(true, note);
  }

  public boolean getPunchedInStatus(Map<String, String> cookies) throws Exception {
    return continuePunchInOut(cookies, false, null);
  }

  public boolean continuePunchInOut(
      Map<String, String> cookies, boolean toPunchIn, Ref<Connection> nextConnectionRef)
      throws Exception {
    final String url;
    if (toPunchIn) {
      url = this.host.resolve(punchIn).toASCIIString();
    } else {
      url = this.host.resolve(punchOut).toASCIIString();
    }

    Connection connection = Jsoup.connect(url).cookies(cookies);
    connection.followRedirects(true);
    Document document = connection.get();
    Element element = document.getElementById("punchTimeForm");

    // ref for below
    // add this to current linnk: ?timeZone="+gmtHours
    TimeZone timeZone = TimeZone.getDefault();
    int gmtHours = timeZone.getRawOffset() / 1000;
    Connection nextConnection = Jsoup.connect(url + "?timeZone=" + gmtHours);
    Map<String, String> inputs = this.parseInputs(element, nextConnection);
    if (nextConnectionRef != null) {
      nextConnectionRef.setRef(nextConnection);
    }

    boolean punchIn = inputs.getOrDefault("button", "In").equalsIgnoreCase("In");
    return punchIn == toPunchIn;
  }

  private boolean punchInOut(boolean toPunchIn, String note) throws Exception {
    Map<String, String> cookies = this.getCookies();
    // Connection
    Ref<Connection> nextConnectionRef = new Ref<>(null);
    // same state.
    if (this.continuePunchInOut(cookies, toPunchIn, nextConnectionRef)) {
      LocalDateTime localDateTime = LocalDateTime.now();
      Connection nextConnection = nextConnectionRef.getRef();
      nextConnection.data("date", getCurrentDate(localDateTime.toLocalDate()));
      nextConnection.data("time", getCurrentTime(localDateTime.toLocalTime()));
      // note
      if (note == null) {
        note = punchInOutNote;
      }
      nextConnection.data("note", note);

      // new submit
      nextConnection.cookies(cookies);
      Document punchTimeResultDocument = nextConnection.post();
      // check if waring class is there
      StringBuilder message = new StringBuilder("");
      Elements warning =
          punchTimeResultDocument.getElementsByAttributeValue("class", "message warning fadable");
      if (warning != null) {
        for (Element element1 : warning) {
          message.append("WARN:").append(element1.text()).append("\n");
        }
      }

      Elements error =
          punchTimeResultDocument.getElementsByAttributeValue("class", "message error");
      if (error != null) {
        for (Element element1 : error) {
          message.append("ERROR:").append(element1.text()).append("\n");
        }
      }

      if (message.length() > 0) {
        throw new Exception(message.toString());
      }
      return true;
    }
    return false;
  }

  private String getCurrentDate(LocalDate localDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return localDate.format(formatter);
  }

  private String getCurrentTime(LocalTime localTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    return localTime.format(formatter);
  }

  public Map<String, String> getCookies() throws IOException {
    String loginUrl = this.host.resolve(this.login).toASCIIString();
    Connection loginConnection = Jsoup.connect(loginUrl);
    Connection.Response response1 = loginConnection.method(Connection.Method.GET).execute();
    Document document = response1.parse();
    Element formElement = document.getElementById("frmLogin");
    URI loginUri = this.host.resolve(formElement.attr("action"));

    Map<String, String> inputs = this.parseInputs(formElement);

    // put username and password
    inputs.put("txtUsername", this.userName);
    inputs.put("txtPassword", this.password);
    // offset
    TimeZone timeZone = TimeZone.getDefault();
    double offset = (double) timeZone.getRawOffset() / (3600000);
    inputs.put("hdnUserTimeZoneOffset", String.valueOf(offset));

    Connection connection = Jsoup.connect(loginUri.toASCIIString());
    connection.method(Connection.Method.POST);
    connection.data(inputs);
    connection.cookies(response1.cookies());

    connection.followRedirects(true);
    Connection.Response loggedIn = connection.execute();
    Map<String, String> cookies = loggedIn.cookies();
    boolean logged = cookies.getOrDefault("Loggedin", "False").equalsIgnoreCase("True");
    return logged ? cookies : null;
  }

  private Map<String, String> parseInputs(Element formElement) {
    return this.parseInputs(formElement, null);
  }

  private Map<String, String> parseInputs(Element formElement, Connection connection) {
    // each input
    Map<String, String> inputs = new HashMap<>();
    for (Element child : formElement.getElementsByTag("input")) {
      if (child.hasAttr("name")) {
        final String name = child.attr("name");
        String value = "";
        if (child.hasAttr("value")) {
          value = child.attr("value");
        }
        inputs.put(name, value);
        if (connection != null) {
          connection.data(name, value);
        }
      }
    }
    return inputs;
  }

  // Linker
  public static void hrmsAuto(Component parent) throws URISyntaxException {
    boolean haveAccount = Account.haveAccount();
    if (haveAccount) {
      PunchInOut punchInOut = new PunchInOut();
      punchInOut.defaultShow(parent);
    } else {
      Account account = new Account();
      account.defaultShow(parent);
    }
  }
}
