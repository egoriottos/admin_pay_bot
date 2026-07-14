package tg.bot.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tg.bot.localization.Language;
import tg.bot.subscription.Subscription;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private Long telegramId;

  private String username;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  private Language language;

  @CreatedDate private LocalDateTime createdAt;

  @OneToMany(mappedBy = "user")
  private List<Subscription> subscriptions = new ArrayList<>();
}
