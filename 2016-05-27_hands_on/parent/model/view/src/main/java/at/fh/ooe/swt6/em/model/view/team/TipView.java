package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by Thomas on 5/26/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
public class TipView extends AbstractEntityView<Long> {

    @Getter
    @Setter
    private String team1Name;
    @Getter
    @Setter
    private String team2Name;
    @Getter
    @Setter
    private Integer goalsTeam1;
    @Getter
    @Setter
    private Integer goalsTeam2;
    @Getter
    @Setter
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
