package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Thomas on 5/27/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
public class UserScoreView extends AbstractEntityView<Long> {

    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private Integer tipsTotal;
    @Getter
    @Setter
    private Integer tipsOpen;
    @Getter
    @Setter
    private Integer tipsWon;
    @Getter
    @Setter
    private Integer tipsLost;

    public UserScoreView(Long id,
                         Long version) {
        super(id, version);
    }

}
