package it.fanciullini.data.entity;
import it.fanciullini.utility.TypeCommunication;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "CommunicationLog")
@Table(name = "communication_log")
public class CommunicationLog implements Serializable
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "type_communication")
    private TypeCommunication typeCommunication;

    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", referencedColumnName = "username")
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", referencedColumnName = "username")
    private User receiver;

    @Column(name = "submission_date")
    private Date submissionDate;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

}
