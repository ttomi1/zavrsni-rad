import {PostComment} from './comment.model';

export interface Post {
  id: number;
  text: string;
  imageUrl: string;
  createdAt: string;
  authorUsername: string;
  authorImageUrl?: string;

  comments?: PostComment[];
  newCommentText?: string;

  showAllComments?: boolean;

  likesCount?: number;
  likedByMe?: boolean;
}
