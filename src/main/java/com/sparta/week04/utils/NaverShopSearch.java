//NaverShopSearch
package com.sparta.week04.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sparta.week04.dto.ItemDto;

@Component
public class NaverShopSearch {
    public String search(String query) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "");
        headers.add("X-Naver-Client-Secret", "");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(
                "https://openapi.naver.com/v1/search/shop.json?query=" + query,
                HttpMethod.GET,
                requestEntity,
                String.class);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();

        if (status == HttpStatus.OK.value()) {
            return response;
        } else if (status == HttpStatus.BAD_REQUEST.value()) {
            throw new IllegalArgumentException("요청 값이 부적절합니다.");
        } else if (status == HttpStatus.NOT_FOUND.value()) {
            throw new NullPointerException("검색 데이터가 존재하지 않습니다.");
        } else {
            throw new RuntimeException();
        }
    }

    public List<ItemDto> fromJSONtoItems(String result) {
        JSONObject resultJson = new JSONObject(result);
        JSONArray items = resultJson.getJSONArray("items");

        List<ItemDto> ret = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            System.out.println(itemJson);
            ItemDto itemDto = new ItemDto(itemJson);
            ret.add(itemDto);
        }

        return ret;
    }


}