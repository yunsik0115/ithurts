package com.sidepj.ithurts.service;

import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DataService<T> {

    // TO DO //
    // DB에서 가져온 결과가 N건 미만인 경우 OpenAPIDataService를 통해 파싱하여 가져옴
    // N건 이상인 경우 OpenAPIDataService 사용 X - 네트워크 트래픽 감소를 위함.

    public List<T> searchByName(String officeName); // 이름에 의해 찾기

    public Page<T> searchByName(String officeName, Pageable pageable);

    public List<T> searchByCity(String cityName); // 도시에 의해 찾기

    public Page<T> searchByCity(String cityName, Pageable pageable);

    public List<T> searchByDetailedCity(String cityName, String detailedCity);

    public Page<T> searchByDetailedCity(String cityName, String detailedCity, Pageable pageable);// 군, 동에 의해 찾기

    public List<T> searchByCoordinateAndRadius(double longitude, double latitude, double radius);

    public Page<T> searchByCoordiateAndRadius(double longitude, double latitude, double radius, Pageable pageable);

    public T findById(Long id);

    // TO - DO : 반경 내에 있는 경우 좌표 주변 동을 모두 크롤링하여 띄우는 메서드
    // A동과 B동 경계에 있는 경우 두 동 모두 데이터 파싱하여 띄우는 역할. 이건 프론트쪽에서 조건 가져오는걸로 컨트롤러에서 N번 호출하도록 해야겠다.


    public List<T> getAll();

    public Page<T> getAll(Pageable pageable);

    public List<T> retrieveAll();

    public List<T> searchOpened(double longitude, double latitude, double radius, boolean holiday);

    public Page<T> searchOpened(double longitude, double latitude, double radius, boolean holiday, Pageable pageable);

    void remove(Long id);

    public void updateEntityFromDTO(Long id, Object instance);
}
