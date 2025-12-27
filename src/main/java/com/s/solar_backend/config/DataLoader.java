package com.s.solar_backend.config;

import com.s.solar_backend.entity.News;
import com.s.solar_backend.entity.User;
import com.s.solar_backend.repository.NewsRepository;
import com.s.solar_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

        private final NewsRepository newsRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                createDefaultAdmin();
                if (newsRepository.count() == 0) {
                        log.info("Tạo sample data cho tin tức...");
                        createSampleNews();
                        log.info("Đã tạo xong sample data!");
                }
        }

        private void createDefaultAdmin() {
                if (userRepository.count() == 0) {
                        log.info("Creating default admin user...");
                        User admin = new User();
                        admin.setUsername("admin");
                        admin.setPassword(passwordEncoder.encode("admin123")); // Default password
                        admin.setRole("ROLE_ADMIN");
                        userRepository.save(admin);
                        log.info("Default admin user created: username=admin, password=admin123");
                }
        }

        private void createSampleNews() {
                // Sample News 1
                News news1 = new News();
                news1.setTitle("HBMP Group Hợp Tác Với Lithium Valley - Lưu Trữ Điện Dân Dụng");
                news1.setShortDescription(
                                "Trong bối cảnh nhu cầu lưu trữ điện mặt trời ngày càng tăng, HBMP Group và Lithium Valley thời gian qua tích cực hợp tác để đem lại giải pháp pin lưu trữ tối ưu cho khách hàng.");
                news1.setContent(
                                "<p>Thời gian gần đây, nhu cầu sử dụng hệ thống lưu trữ năng lượng ngày càng tăng cao. Việc kết hợp hệ thống điện mặt trời với pin lưu trữ không chỉ giúp tối ưu hóa hiệu suất sử dụng điện mà còn đảm bảo nguồn cung cấp điện ổn định 24/7.</p>"
                                                +
                                                "<h3>Ưu điểm của hệ thống lưu trữ năng lượng</h3>" +
                                                "<ul><li>Sử dụng điện mặt trời ngay cả khi không có ánh nắng</li>" +
                                                "<li>Tiết kiệm đến 90% chi phí điện hàng tháng</li>" +
                                                "<li>Đảm bảo nguồn điện dự phòng khi lưới điện gặp sự cố</li></ul>" +
                                                "<p>HBMP Group cam kết mang đến những sản phẩm và dịch vụ tốt nhất cho khách hàng trong lĩnh vực năng lượng tái tạo.</p>");
                news1.setImageUrl("/photo/anh39.jpg");
                news1.setAuthor("Admin HBMP");
                news1.setCategory(News.NewsCategory.COMPANY_NEWS);
                news1.setIsFeatured(true);
                news1.setIsPublished(true);
                news1.setViews(245L);
                news1.setCreatedAt(LocalDateTime.now().minusDays(5));
                news1.setPublishedAt(LocalDateTime.now().minusDays(5));
                newsRepository.save(news1);

                // Sample News 2
                News news2 = new News();
                news2.setTitle("Năng lượng mặt trời là tương lai năm 2024: nghiên cứu");
                news2.setShortDescription(
                                "Theo báo cáo mới nhất, năng lượng mặt trời đang trở thành xu hướng chủ đạo trong việc chuyển đổi năng lượng toàn cầu.");
                news2.setContent(
                                "<p>Năng lượng mặt trời đã và đang khẳng định vị thế là nguồn năng lượng của tương lai. Với những tiến bộ vượt bậc trong công nghệ và sự giảm giá đáng kể của các tấm pin mặt trời, ngành công nghiệp này đang chứng kiến sự tăng trưởng mạnh mẽ.</p>"
                                                +
                                                "<h3>Các xu hướng năm 2024</h3>" +
                                                "<p>1. Công nghệ pin mặt trời hiệu suất cao<br>" +
                                                "2. Hệ thống lưu trữ năng lượng thông minh<br>" +
                                                "3. Giải pháp năng lượng cho nông nghiệp</p>");
                news2.setImageUrl("/photo/anh40.jpg");
                news2.setAuthor("Chuyên gia năng lượng");
                news2.setCategory(News.NewsCategory.INDUSTRY_NEWS);
                news2.setIsFeatured(false);
                news2.setIsPublished(true);
                news2.setViews(189L);
                news2.setCreatedAt(LocalDateTime.now().minusDays(3));
                news2.setPublishedAt(LocalDateTime.now().minusDays(3));
                newsRepository.save(news2);

                // Sample News 3
                News news3 = new News();
                news3.setTitle("Công nghệ pin mặt trời mới với hiệu suất 25%");
                news3.setShortDescription(
                                "Breakthrough trong công nghệ pin mặt trời với hiệu suất chuyển đổi năng lượng đạt 25%, mở ra kỷ nguyên mới cho năng lượng tái tạo.");
                news3.setContent(
                                "<p>Các nhà nghiên cứu vừa công bố thành công trong việc phát triển công nghệ pin mặt trời thế hệ mới với hiệu suất chuyển đổi năng lượng lên đến 25%. Đây là bước tiến quan trọng trong ngành năng lượng tái tạo.</p>"
                                                +
                                                "<h3>Đặc điểm nổi bật</h3>" +
                                                "<ul><li>Hiệu suất cao hơn 30% so với pin truyền thống</li>" +
                                                "<li>Tuổi thọ lên đến 30 năm</li>" +
                                                "<li>Khả năng hoạt động tốt trong điều kiện ánh sáng yếu</li></ul>");
                news3.setImageUrl("/photo/anh41.jpg");
                news3.setAuthor("R&D Team");
                news3.setCategory(News.NewsCategory.TECHNOLOGY);
                news3.setIsFeatured(true);
                news3.setIsPublished(true);
                news3.setViews(321L);
                news3.setCreatedAt(LocalDateTime.now().minusDays(2));
                news3.setPublishedAt(LocalDateTime.now().minusDays(2));
                newsRepository.save(news3);

                // Sample News 4
                News news4 = new News();
                news4.setTitle("Dự án điện mặt trời tại Nhà máy Saitex 5 & 6 (KCN Amata, Đồng Nai)");
                news4.setShortDescription(
                                "HBMP Group hoàn thành thành công dự án điện mặt trời áp mái công suất 2.5MW tại Nhà máy Saitex, góp phần tiết kiệm chi phí điện cho doanh nghiệp.");
                news4.setContent(
                                "<p>Dự án điện mặt trời áp mái tại Nhà máy Saitex 5 & 6 được HBMP Group triển khai và hoàn thành thành công với tổng công suất 2.5MW. Đây là một trong những dự án điển hình cho việc ứng dụng năng lượng mặt trời trong sản xuất công nghiệp.</p>"
                                                +
                                                "<h3>Thông tin dự án</h3>" +
                                                "<ul><li>Công suất: 2.5MW</li>" +
                                                "<li>Số lượng tấm pin: 5,200 tấm</li>" +
                                                "<li>Tiết kiệm: 80% chi phí điện hàng năm</li>" +
                                                "<li>Thời gian hoàn vốn: 5-6 năm</li></ul>" +
                                                "<p>Dự án đã đi vào hoạt động và mang lại hiệu quả kinh tế cao cho nhà máy.</p>");
                news4.setImageUrl("/photo/anh18.jpg");
                news4.setAuthor("Project Manager");
                news4.setCategory(News.NewsCategory.PROJECT_NEWS);
                news4.setIsFeatured(true);
                news4.setIsPublished(true);
                news4.setViews(456L);
                news4.setCreatedAt(LocalDateTime.now().minusDays(7));
                news4.setPublishedAt(LocalDateTime.now().minusDays(7));
                newsRepository.save(news4);

                // Sample News 5 - Draft
                News news5 = new News();
                news5.setTitle("Hướng dẫn lắp đặt hệ thống điện mặt trời cho hộ gia đình");
                news5.setShortDescription(
                                "Bài viết hướng dẫn chi tiết các bước lắp đặt hệ thống điện mặt trời cho hộ gia đình, từ khảo sát đến vận hành.");
                news5.setContent(
                                "<p>Việc lắp đặt hệ thống điện mặt trời cho hộ gia đình cần tuân thủ đúng quy trình để đảm bảo hiệu quả và an toàn.</p>"
                                                +
                                                "<h3>Các bước thực hiện</h3>" +
                                                "<ol><li>Khảo sát và thiết kế</li>" +
                                                "<li>Xin phép cơ quan chức năng</li>" +
                                                "<li>Thi công lắp đặt</li>" +
                                                "<li>Kiểm tra và vận hành thử nghiệm</li></ol>");
                news5.setImageUrl("/photo/anh9.jpg");
                news5.setAuthor("Technical Team");
                news5.setCategory(News.NewsCategory.TECHNOLOGY);
                news5.setIsFeatured(false);
                news5.setIsPublished(false); // Draft
                news5.setViews(0L);
                news5.setCreatedAt(LocalDateTime.now().minusDays(1));
                newsRepository.save(news5);

                // Sample News 6
                News news6 = new News();
                news6.setTitle("Thị trường năng lượng mặt trời Việt Nam tăng trưởng mạnh");
                news6.setShortDescription(
                                "Theo báo cáo thị trường, ngành năng lượng mặt trời Việt Nam đạt mức tăng trường 45% trong năm 2024, dẫn đầu khu vực Đông Nam Á.");
                news6.setContent(
                                "<p>Thị trường năng lượng mặt trời Việt Nam đang chứng kiến sự tăng trưởng ấn tượng với mức tăng 45% so với năm trước. Đây là tín hiệu tích cực cho ngành năng lượng tái tạo trong nước.</p>"
                                                +
                                                "<h3>Những yếu tố thúc đẩy</h3>" +
                                                "<ul><li>Chính sách hỗ trợ từ Chính phủ</li>" +
                                                "<li>Giá điện mặt trời ngày càng cạnh tranh</li>" +
                                                "<li>Ý thức môi trường của người dân tăng cao</li></ul>");
                news6.setImageUrl("/photo/anh42.jpg");
                news6.setAuthor("Market Analyst");
                news6.setCategory(News.NewsCategory.MARKET_NEWS);
                news6.setIsFeatured(false);
                news6.setIsPublished(true);
                news6.setViews(178L);
                news6.setCreatedAt(LocalDateTime.now().minusDays(4));
                news6.setPublishedAt(LocalDateTime.now().minusDays(4));
                newsRepository.save(news6);
        }
}