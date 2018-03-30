package com.psl.integrador.service;

import com.psl.integrador.exception.EntityNotFoundException;
import com.psl.integrador.model.Topic;
import com.psl.integrador.model.enums.Status;
import com.psl.integrador.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final CollaboratorService collaboratorService;
    private final NotificationService notificationService;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, CollaboratorService collaboratorService,
                            NotificationService notificationService) {
        this.topicRepository = topicRepository;
        this.collaboratorService = collaboratorService;
        this.notificationService = notificationService;
    }

    @Override
    public List<Topic> getTopicsByStatus(int status) {
        // Check if status exists in enum
        if (status < 0 || Status.values().length - 1 < status)
            throw new ArrayIndexOutOfBoundsException(status);

        return topicRepository.findTopicByStatusOrderByCreatedAtDesc(Status.values()[status]);
    }

    @Override
    public Topic add(Topic topic) {
        return topicRepository.insert(topic);
    }

    @Override
    public Topic update(Topic topic) throws EntityNotFoundException {

        Topic dbTopic = topicRepository.findTopicById(topic.getId());

        if (dbTopic == null)
            throw new EntityNotFoundException(String.format("Topic with id: %s was not found", topic.getId()));

        if (topic.getStatus() != dbTopic.getStatus()) {
            int tipo;
            if(dbTopic.getStatus() == Status.toOpen && topic.getStatus() == Status.opened){
                //abierto
                tipo = 1;
            }else if (dbTopic.getStatus() == Status.toOpen && topic.getStatus() == Status.closed){
                //nunca abrio
                tipo = 2;
            }else{
                //cerro un grupo abierto
                tipo =3;
            }
            dbTopic.setStatus(topic.getStatus());

            //Send notifications to all collaborators
            sendNotification(topic,tipo);
        }

        dbTopic.setChat(topic.getChat());

        return topicRepository.save(dbTopic);
    }

    @Override
    public Topic getTopicById(String id) {
        return topicRepository.findTopicById(id);
    }

    private void sendNotification(Topic topic,int tipo) {

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                notificationService.sendNotification(collaboratorService.getCollaboratorsByTopic(topic),topic, tipo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }

}