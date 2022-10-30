package com.wang.blog.service;

import com.wang.blog.dao.CommentRepository;
import com.wang.blog.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys =new ArrayList<>();

    /**
     * 通过博客和升序时间查找评论列表
     * @param blogId
     * @return
     */
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //按照时间来升序排序
        Sort sort = Sort.by("createTime");
        //根据博客id和排序规则得到相应的评论列表
        List<Comment> comments =commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);

        return eachComment(comments);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Override
    public Comment saveComment(Comment comment) {
        //获取comment父对象的id
        Long parentCommentId = comment.getParentComment().getId();
        //判断comment父对象id等于-1
        if (parentCommentId !=-1){
            //不等于-1说明不为空,添加的该评论为回复状态
           comment.setParentComment(commentRepository.findById(parentCommentId).get());
        }else{
            //等于-1就set空值，添加的评论不是回复状态
            comment.setParentComment(null);
        }
        //添加创建时间
        comment.setCreateTime(new Date());
        //保存评论
        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *  合并所有子代评论，全部归为二级评论
     * @param comments root根节点，blog节点不为空的对象集合。
     */
    private void combineChildren(List<Comment> comments){
        for (Comment comment:comments){
            //获得每个评论的子评论列表
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1:replys1){
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }


    /**
     * 递归迭代，
     * @param comment
     */
    private void recursively(Comment comment){
        //顶节点添加到临时存放集合
        tempReplys.add(comment);
        //如果顶级节点下还有子评论
        if (comment.getReplyComments().size()>0){
            //得到子评论的列表
            List<Comment> replys = comment.getReplyComments();
            //遍历子评论列表
            for (Comment reply:replys){

                tempReplys.add(reply);
                //如果子评论下还有子评论，继续递归调用
                if (reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }

}
