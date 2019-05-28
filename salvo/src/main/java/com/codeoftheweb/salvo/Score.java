package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    


    private float score;

    private LocalDateTime finishDate;
}
