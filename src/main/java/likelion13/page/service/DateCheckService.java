package likelion13.page.service;

import likelion13.page.domain.DateCheck;
import likelion13.page.repository.DateCheckRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class DateCheckService {

    private final DateCheckRepository dateCheckRepository;

    @Value("${api-key.date-checker}")
    private String dateCheckerKey;

    @Transactional
    public DateCheck getDayCheck(LocalDate date){
        DateCheck buzDay = dateCheckRepository.findByDate(date);
        if(buzDay != null){
            return buzDay;
        }

        buzDay = new DateCheck(date, nextBuzDay(date.plusDays(1)), nextBuzDay(date.plusDays(7)));
        dateCheckRepository.save(buzDay);

        return buzDay;
    }
    @Transactional(readOnly = true)
    public LocalDateTime needReceiveDate(LocalDateTime now) {
        now = now.with(getDayCheck(now.toLocalDate()).getNextBizDay());
        if (now.getDayOfWeek().getValue() == 5) { // 금요일인 경우
            now = now.with(LocalTime.of(15, 0,0));
        } else {
            now = now.with(LocalTime.of(17, 30,0));
        }

        return now;
    }

    @Transactional(readOnly = true)
    public LocalDateTime needReturnDate(LocalDateTime now){
        now = now.with(getDayCheck(now.toLocalDate()).getNextWeekBizDay());
        return now;
    }

    @Transactional
    public LocalDate beforeBuzDay(LocalDate date){
        LocalDate beforeDate = date.minusDays(1);
        while(!getDayCheck(beforeDate.minusDays(1)).getNextBizDay().isBefore(date)){
            beforeDate = beforeDate.minusDays(1);
        }
        System.out.println("beforeDate = " + beforeDate);
        return beforeDate;
    }

    public LocalDate nextBuzDay(LocalDate date){
        try {
            while((isWeekend(date) || isHoliday(date))){
                date = date.plusDays(1);
            }
        }catch (Exception e){
            System.out.println("e = " + e);
        }
        return date;
    }

    private boolean isWeekend(LocalDate date){
        return date.getDayOfWeek().getValue() >= 6;
    }

    private boolean isHoliday(LocalDate date) throws Exception {
        String response = getAPIResponse(getHolidayUrl(date));
        return parseHolidayResponse(response, date);
    }

    private String getHolidayUrl(LocalDate date) {
        return "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo" +
                "?serviceKey=" + dateCheckerKey +
                "&solYear=" + date.getYear() +
                "&solMonth=" + String.format("%02d", date.getMonthValue()) +
                "&_type=json";
    }

    private String getAPIResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.disconnect();

        return response.toString();
    }

    private boolean parseHolidayResponse(String response, LocalDate date) {
        JSONObject jsonResponse = new JSONObject(response);

        Object itemObject = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").get("item");

        JSONArray holidayList = new JSONArray();
        if (itemObject instanceof JSONArray) {
            holidayList = (JSONArray) itemObject;
        } else if (itemObject instanceof JSONObject) {
            holidayList.put((JSONObject) itemObject);
        }

        String targetDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        for (int i = 0; i < holidayList.length(); i++) {
            JSONObject holiday = holidayList.getJSONObject(i);
            Integer locdate = holiday.getInt("locdate"); // 공휴일 날짜
            if (targetDate.equals(locdate.toString())) {
                return true;
            }
        }
        return false;
    }

}