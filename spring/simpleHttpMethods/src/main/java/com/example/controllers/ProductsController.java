package com.example.controllers;

import com.example.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.services.ProductService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductsController {

    private final ProductService productService;

    // We use DI through the controller’s constructor parameters
    // to get the service bean from the Spring context.
    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    // We map the controller action to the /products path. The
    // @RequestMapping annotation, by default, uses the HTTP GET method.
    @RequestMapping("/products")
    public String viewProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products.html";
    }

    // We map the controller action
    // to the /products path. We use
    // the method attribute of the
    // @RequestMapping annotation to
    // change the HTTP method to POST.
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public String addProduct(
            @RequestParam String name,
            @RequestParam double price,
            Model model
    )
    {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        productService.addProduct(p);

        var products = productService.findAll();
        model.addAttribute("products", products);

        return "products.html";
    }
}
