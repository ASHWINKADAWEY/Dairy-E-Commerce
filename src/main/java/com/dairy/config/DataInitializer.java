package com.dairy.config;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dairy.model.Product;
import com.dairy.model.User;
import com.dairy.repository.ProductRepository;
import com.dairy.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        // ── Create default admin ─────────────────────────────────────────
        if (!userRepository.existsByEmail("FarmsPureDairy@gmail.com")) {
            User admin = User.builder()
                    .fullName("Admin")
                    .email("FarmsPureDairy@gmail.com")
                    .phone("9999999991")
                    .password("FarmPure@123")
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("========================================");
            System.out.println("  DEFAULT ADMIN CREATED");
            System.out.println("  Email   : FarmsPureDairy@gmail.com");
            System.out.println("  Password: FArmPure@123");
            System.out.println("========================================");
        }

        // ── Seed sample products if none exist ──────────────────────────
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder()
                    .name("Fresh Full Cream Milk").description("Pure farm-fresh full cream milk, rich in calcium and vitamins. 1 Litre pack.")
                    .price(new BigDecimal("65.00")).quantity(100).category("Milk").build());

            productRepository.save(Product.builder()
                    .name("Skimmed Milk").description("Low-fat skimmed milk, perfect for health-conscious consumers. 1 Litre pack.")
                    .price(new BigDecimal("58.00")).quantity(80).category("Milk").build());

            productRepository.save(Product.builder()
                    .name("FarmPure Butter (500g)").description("Pasteurised butter made from fresh cream. Rich taste and smooth texture.")
                    .price(new BigDecimal("245.00")).quantity(60).category("Butter").build());

            productRepository.save(Product.builder()
                    .name("Desi Ghee (1kg)").description("100% pure cow ghee, traditionally churned. Ideal for cooking and health.")
                    .price(new BigDecimal("680.00")).quantity(40).category("Ghee").build());

            productRepository.save(Product.builder()
                    .name("Paneer (200g)").description("Fresh soft paneer made from full-cream milk. Perfect for curries and snacks.")
                    .price(new BigDecimal("95.00")).quantity(50).category("Paneer").build());

            productRepository.save(Product.builder()
                    .name("Strawberry Yoghurt (400g)").description("Creamy yoghurt with real strawberry chunks. Chilled and ready to eat.")
                    .price(new BigDecimal("75.00")).quantity(70).category("Yoghurt").build());

            productRepository.save(Product.builder()
                    .name("Natural Greek Yoghurt (400g)").description("Thick, probiotic-rich Greek yoghurt. Great with granola or as a dip.")
                    .price(new BigDecimal("85.00")).quantity(65).category("Yoghurt").build());

            productRepository.save(Product.builder()
                    .name(" Shrikhand (200g)").description("Soft creamy perfect dessert.")
                    .price(new BigDecimal("185.00")).quantity(35).category("Shrikhand").build());

            productRepository.save(Product.builder()
                    .name("Butterscotch Shrikhand (200g)").description("Fresh Shrikhand with sharp and tangy flavour.")
                    .price(new BigDecimal("175.00")).quantity(30).category("Shrikhand").build());

            productRepository.save(Product.builder()
                    .name("Plain Lassi (1L)").description("Creamy lassi made with real milk and natural extract.")
                    .price(new BigDecimal("195.00")).quantity(45).category("Lassi").build());

            productRepository.save(Product.builder()
                    .name("Mango Lassi (1L)").description("Rich and creamy mango lassi. A family favourite!")
                    .price(new BigDecimal("210.00")).quantity(40).category("Lassi").build());

            productRepository.save(Product.builder()
                    .name("Fresh Cream (200ml)").description("Light whipping cream ideal for desserts, soups, and sauces.")
                    .price(new BigDecimal("55.00")).quantity(90).category("Cream").build());
            
            productRepository.save(Product.builder()
                    .name("Butter Milk (200ml)").description("\"The Desi Refresher: Fresh, Healthy, & Much More.")
                    .price(new BigDecimal("15.00")).quantity(90).category("Dairy Drinks").build());

            System.out.println("  Sample products seeded successfully.");
        }
    }
}
