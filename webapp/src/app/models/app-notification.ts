import { NotificationType } from "./notification-type";

export class AppNotification {
  notificationDate: Date;
  type: NotificationType;
  summary?: string;
  content: string;
  recommendations?: string;
  visible?: boolean;

  constructor(notificationDate: Date, type: NotificationType, summary: string,
              content: string, recommendations: string, visible: boolean) {
    this.notificationDate = notificationDate;
    this.type = type;
    this.summary = summary;
    this.content = content;
    this.recommendations = recommendations;
    this.visible = visible;
  }
}

