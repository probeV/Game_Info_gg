package probeV.GameInfogg.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import probeV.GameInfogg.controller.admin.dto.request.*;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;
import probeV.GameInfogg.service.StorageService;
import probeV.GameInfogg.service.admin.AdminService;

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
        @RequestBody ItemSaveRequestDto requestDto
    ){
        adminService.createItems(requestDto);
    }


    // 아이템 항목 수정
    @PutMapping("/admins/items/{id}")
    public void updateItems(
            @PathVariable(value = "id") Long itemId,
            @RequestBody ItemUpdateRequestDto requestDto
    ){
        adminService.updateItems(itemId, requestDto);
    }

    // 아이템 항목 삭제
    @DeleteMapping("/admins/items/{id}")
    public void deleteItems(
        @PathVariable(value = "id") Long itemId
    ){
        adminService.deleteItems(itemId);
    }

    // 파일 생성
    @PostMapping("/admins/files")
    public String createImages(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "FileSaveRequestDto") FileSaveRequestDto requestDto
    ) throws IOException{
        return storageService.createImageFile(file, requestDto.getDirectoryPath());
    }

    // 파일 수정
    @PutMapping("/admins/files")
    public String updateImages(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "FileUpdateRequestDto") FileUpdateRequestDto requestDto
    ) throws IOException{
        return storageService.updateFile(file,requestDto.getPreFileUrl(), requestDto.getDirectoryPath());
    }


    // 파일 삭제
    @DeleteMapping("/admins/files")
    public void deleteImages(
            @RequestBody FileDeleteRequestDto requestDto
    )throws IOException{
        storageService.deleteFile(requestDto.getImageUrl());
    }

}
