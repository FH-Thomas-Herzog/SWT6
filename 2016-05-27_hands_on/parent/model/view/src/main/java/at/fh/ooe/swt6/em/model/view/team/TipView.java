package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by Thomas on 5/26/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipView extends EntityView<Long> {

    private String team1Name;
    private String team2Name;
    private Integer goalsTeam1;
    private Integer goalsTeam2;
    private LocalDateTime gameDate;


    public TipView(Long id) {
        this.id = id;
    }

    public TipView(Long id,
                   Long version) {
        this.id = id;
        this.version = version;
    }
}
