package probeV.GameInfogg.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListDeleteRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.ItemUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;
import probeV.GameInfogg.domain.item.Item;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.exception.item.ItemNotFoundException;
import probeV.GameInfogg.exception.task.TaskNotFoundException;
import probeV.GameInfogg.repository.item.ItemRepository;
import probeV.GameInfogg.repository.task.DefaultTaskRepository;
import probeV.GameInfogg.repository.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DefaultTaskRepository defaultTaskRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    // 기본 숙제 체크 리스트 항목 생성
    @Override
    @Transactional
    public void saveTasks(List<DefaultTaskListSaveorUpdateRequestDto> requestDto) {
        log.info("AdminService : saveTasks");

        // DB에서 현재 존재하는 Task ID 목록 가져오기
        List<DefaultTask> existingTasks = defaultTaskRepository.findAll();

        // 존재하는 Task ID를 Key로, Task를 Value로 하는 Map 생성
        Map<Integer, DefaultTask> taskMap = existingTasks.stream()
            .collect(Collectors.toMap(DefaultTask::getId, Function.identity()));

        // 수정 및 생성 처리
        for (DefaultTaskListSaveorUpdateRequestDto dto : requestDto) {
            Integer id = dto.getId();

            //log.info(dto.getId(), dto.getName(), dto.getMode(), dto.getFrequency(), dto.getEvent());

            // 이미 존재하는 경우
            if (id != null && taskMap.containsKey(id)) {
                // 수정 로직 호출
                log.info("saveTasks 수정 로직 호출" + id);

                DefaultTask task = taskMap.get(id);
                task.update(dto.getName(), EventType.fromString(dto.getEvent()));
            } 

            // 존재하지 않는 경우
            else if (id == null) {
                // 생성 로직
                log.info("saveTasks 생성 로직 호출" + id);

                defaultTaskRepository.save(dto.toEntity());
            }
            else {
                log.error("saveTasks 오류 발생" + id);

                throw new TaskNotFoundException("Task not found");
            }
        }
    }

    // 기본 숙제 체크 리스트 항목 삭제 
    // requestDto는 현재 존재하는 모든 Task의 ID를 가지고 있음, 따라서 db에는 있고, requestDto에는 없는 경우 삭제    
    @Override
    @Transactional
    public void deleteTasks(List<DefaultTaskListDeleteRequestDto> requestDto) {
        log.info("AdminService : deleteTasks");

        List<DefaultTask> existingTasks = defaultTaskRepository.findAll();

        // 존재하는 Task ID 목록 가져오기
        Set<Integer> existingTaskIds = existingTasks.stream()
            .map(DefaultTask::getId)
            .collect(Collectors.toSet());

        // 존재하는 Task ID를 Key로, Task를 Value로 하는 Map 생성
        Map<Integer, DefaultTask> taskMap = existingTasks.stream()
            .collect(Collectors.toMap(DefaultTask::getId, Function.identity()));

        for (DefaultTaskListDeleteRequestDto dto : requestDto) {
            if(taskMap.get(dto.getId()) != null) {
                existingTaskIds.remove(dto.getId());
            }
        }

        for(Integer taskId : existingTaskIds) {
            log.info("deleteTasks 삭제 로직 호출");
            if(taskMap.get(taskId) != null) {
                defaultTaskRepository.deleteById(taskId);
            }
        }
    }

    // 유저 목록 조회
    @Override
    public UserPageResponseDto getUserList(int page) {
        log.info("getUserList 유저 목록 조회");

        Pageable pageable = PageRequest.of(page, 10);

        Page<User> entities = userRepository.findAll(pageable);

        return new UserPageResponseDto(entities);
    }

    // 아이템 항목 생성
    @Override
    @Transactional
    public void createItems(ItemSaveRequestDto requestDto) {
        // 아이템 항목을 생성하는 로직을 구현합니다.
        log.info("createItems 아이템 항목 생성");

        Item item = requestDto.toEntity();
        itemRepository.save(item);
    }

    // 아이템 항목 수정
    @Override
    @Transactional
    public void updateItems(Long itemId, ItemUpdateRequestDto requestDto) {
        // 아이템 항목을 수정하는 로직을 구현합니다.
        log.info("updateItems 아이템 항목 수정");

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        log.info(item.getId() + " " + item.getName());
        item.update(requestDto.getName(), requestDto.getEffect(), requestDto.getDescription(), requestDto.getImageUrl());
    }

    // 아이템 항목 삭제
    @Override
    @Transactional
    public void deleteItems(Long itemId) {
        // 아이템 항목을 삭제하는 로직을 구현합니다.
        log.info("deleteItems 아이템 항목 삭제");

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        itemRepository.delete(item);
    }
}
