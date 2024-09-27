package probeV.GameInfogg.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import org.springframework.web.multipart.MultipartFile;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListDeleteRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemDeleteRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemUpdateRequestDto;
import probeV.GameInfogg.service.StorageService;
import probeV.GameInfogg.service.admin.AdminService;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {
    private final AdminService adminService;
    private final StorageService storageService;


    // 기본 숙제 체크 리스트 항목 설정 (생성, 수정)
    @PostMapping("/admins/tasks")
    public void saveOrUpdateTasks(
        @Valid @RequestBody List<DefaultTaskListSaveorUpdateRequestDto> requestDto
    ){
        adminService.saveTasks(requestDto);

        return;
    }

    // 기본 숙제 체크 리스트 항목 삭제
    @DeleteMapping("/admins/tasks")
    public void deleteTasks(
        @Valid @RequestBody List<DefaultTaskListDeleteRequestDto> requestDto
    ){
        adminService.deleteTasks(requestDto);

        return;
    }

    // 유저 목록 조회
    @GetMapping("/admins/users")
    public UserPageResponseDto getUserList(@RequestParam(value="page", defaultValue="0") int page) {
        UserPageResponseDto userPage = adminService.getUserList(page);

        return userPage;
    }

    // 아이템 항목 생성
    @PostMapping("/admins/items")
    public void createItems(
        @RequestParam(value = "file")MultipartFile file,
        @RequestBody ItemSaveRequestDto requestDto
    ) throws IOException{
        String directory = "items";
        String url = storageService.createFile(file, directory);

        adminService.createItems(url, requestDto);
    }


    // 아이템 항목 수정
    @PutMapping("/admins/items/{id}")
    public void updateItems(
            @PathVariable(value = "id") Long itemId,
            @RequestParam(value = "file")MultipartFile file,
            @RequestBody ItemUpdateRequestDto requestDto
    ) throws IOException{
        String directory = "items";
        String url = storageService.updateFile(file, requestDto.getPreImageUrl(), directory);

        adminService.updateItems(itemId, url, requestDto);
    }

    // 아이템 항목 삭제
    @DeleteMapping("/admins/items/{id}")
    public void deleteItems(
        @PathVariable(value = "id") Long itemId,
        @RequestBody ItemDeleteRequestDto requestDto
    ) throws IOException{
        storageService.deleteFile(requestDto.getUrl());
        adminService.deleteItems(itemId);
    }

}
