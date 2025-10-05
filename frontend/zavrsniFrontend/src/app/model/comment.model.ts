export interface PostComment {
  id: number;
  text: string;
  authorUsername: string;
  authorImageUrl?: string;
  createdAt: string; // ISO string
}
