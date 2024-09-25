package probeV.GameInfogg.controller.item;

import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemIndexCotroller {

    @GetMapping("/item")
    public String item() {
        return "pages/itemlist/itemlist";
    }
}
