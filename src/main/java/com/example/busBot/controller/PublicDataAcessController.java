package com.example.busBot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busBot.dto.BusArrival;
import com.example.busBot.dto.BusRoute;
import com.example.busBot.dto.BusStation;
import com.example.busBot.service.PublicDataAcessServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class PublicDataAcessController {

	@Autowired PublicDataAcessServiceImp pDataAcess;

	/*
	 * 공공데이터포털 APIKEY
	 */
	@GetMapping("/busArrival")
	public String[] busArrival () {
		// 노선번호목록 list
		List<BusRoute> busRouteList = new ArrayList<BusRoute>();
		// 경유정류소목록 list
		List<BusStation> busStationRouteList = new ArrayList<BusStation>();
		// 버스도착정보목록 list
		List<BusArrival> busArrivalList = new ArrayList<BusArrival>();

		// 경유정류소목록 json 데이터
		String busRouteStationListJson = "";
		String[] text = new String[0];
		try {
			/*
			 * 버스 도착예정시간 FLOW
			 * 1. 버스 노선 조회>노선번호목록조회에서 routeId(노선ID)값을 구한다.
			 * 2. 버스 노선 조회 >경유정류소목록조회에서 routeId값을 파라미터로 넘겨 stationId(정류소ID)값을 구한다.
			 * 3. 버스 도착 정보 조회>버스도착정보목록조회에서 stationId값을 파라미터로 넘겨 버스 도착 정보를 조회
			 * 4. 1번에서 구한 routeId값으로 3번에서 조회한 버스도착정보목록에서 검색하여 predictTime1(도착시간(분단위))을 구한다.
			 */

			// 노선번호목록조회 (버스 번호, 지역)
			//busRouteList = pDataAcess.getBusRouteList(busNumber, busRegion);

			// 경유정류소목록조회
			//busStationRouteList = pDataAcess.getBusRouteStationList(busRouteList.get(0).getRouteId(), busStationName);

			busArrivalList = pDataAcess.getBusArrivalList("203000078");
			ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
			busRouteStationListJson = mapper.writeValueAsString(busArrivalList);
			Gson gson = new Gson();
			BusArrival[] busArrivalArray = gson.fromJson(busRouteStationListJson, BusArrival[].class);
			busArrivalList = Arrays.asList(busArrivalArray);

			text = new String[busArrivalList.size()];
			int count = 0;
			for (int i = 0; i < busArrivalList.size(); i++) {
				count += 1;
				System.out.println("(" + count + ") " + "첫번째 도착: " + busArrivalList.get(i).getPredictTime1());
				System.out.println("(" + count + ") " + "두번째 도착: " + busArrivalList.get(i).getPredictTime2());
				text[i] = "(" + count + ") " + "첫번째 도착: " + busArrivalList.get(i).getPredictTime1()  +
						"\n" + "(" + count + ")" + "두번째 도착: " + busArrivalList.get(i).getPredictTime2();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;

	}
}
