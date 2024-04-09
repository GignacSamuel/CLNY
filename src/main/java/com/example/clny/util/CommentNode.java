package com.example.clny.util;

import com.example.clny.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentNode {
    private CommentDTO comment;
    private List<CommentNode> replies;

    public CommentNode(CommentDTO comment) {
        this.comment = comment;
        this.replies = new ArrayList<>();
    }

    public void addReply(CommentNode reply) {
        this.replies.add(reply);
    }

}
