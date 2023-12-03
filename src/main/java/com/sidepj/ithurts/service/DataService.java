package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;

import java.util.List;

public interface DataService<T> {

    // TO DO //
    // DB에서 가져온 결과가 N건 미만인 경우 OpenAPIDataService를 통해 파싱하여 가져옴
    // N건 이상인 경우 OpenAPIDataService 사용 X - 네트워크 트래픽 감소를 위함.

    public List<T> searchByName(String officeName); // 이름에 의해 찾기

    public List<T> searchByCity(String cityName); // 도시에 의해 찾기

    public List<T> searchByDetailedCity(String cityName, String detailedCity); // 군, 동에 의해 찾기

    public List<T> searchByCoordinateAndRadius(double longitude, double latitude, double radius);

    public T findById(Long id);

    public List<T> searchByServiceType(String serviceType);  // 병.의원 타입에 의해 찾기
    // 약국에서 호출하는 경우 예외처리

    // TO - DO : 반경 내에 있는 경우 좌표 주변 동을 모두 크롤링하여 띄우는 메서드
    // A동과 B동 경계에 있는 경우 두 동 모두 데이터 파싱하여 띄우는 역할. 이건 프론트쪽에서 조건 가져오는걸로 컨트롤러에서 N번 호출하도록 해야겠다.

    public List<T> searchBySearchCondition(SearchCondition searchCondition);

    public List<T> getAll();

    public List<T> retrieveAll();


}
