package sia.tacocloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sia.tacocloud.service.OrderAdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private OrderAdminService orderAdminService;

    @Autowired
    public AdminController(OrderAdminService orderAdminService) {
        this.orderAdminService = orderAdminService;
    }

    @PostMapping("/deleteOrders")
    public String deleteAllOrders() {
        orderAdminService.deleteAllUsers();
        return "redirect:admin";
    }
}
