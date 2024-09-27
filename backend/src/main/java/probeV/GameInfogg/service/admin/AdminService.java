package probeV.GameInfogg.service.admin;

import org.springframework.stereotype.Service;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListDeleteRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;

import java.util.List;


@Service
public interface AdminService {

    // 기본 숙제 체크 리스트 항목 (생성, 수정, 삭제
    public void saveTasks(List<DefaultTaskListSaveorUpdateRequestDto> requestDto);

    // 기본 숙제 체크 리스트 항목 삭제
    public void deleteTasks(List<DefaultTaskListDeleteRequestDto> requestDto);

    // 유저 목록 조회
    public UserPageResponseDto getUserList(int page);

    // 아이템 항목 생성
    public void createItems(ItemSaveRequestDto requestDto);

    // 아이템 항목 수정
    public void updateItems(Long itemId, ItemUpdateRequestDto requestDto);

    // 아이템 항목 삭제
    public void deleteItems(Long itemId);

}
