export class AppUser {
  username: string;
  name: string;
  level: string;
  profileImageSrc?: string;

  constructor(username: string, name: string, level: string) {
    this.username = username;
    this.name = name;
    this.level = level;
  }
}
