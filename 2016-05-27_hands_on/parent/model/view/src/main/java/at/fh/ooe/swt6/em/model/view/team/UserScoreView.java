package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Thomas on 5/27/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScoreView extends EntityView<Long> {

    private String email;
    private Integer tipsTotal;
    private Integer tipsOpen;
    private Integer tipsWon;
    private Integer tipsLost;

    public UserScoreView(Long id,
                         Long version) {
        super(id, version);
    }

}
