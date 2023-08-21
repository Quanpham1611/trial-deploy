package quan.dodomilktea.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private String senderName;
    private String senderEmail;
    private String senderPhone;
    private String senderMessage;
    private String accountId;
}
