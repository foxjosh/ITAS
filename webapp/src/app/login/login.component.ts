import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MessageLevel} from "../models/message-level.enum";
import {AuthenticationService} from "../authentication/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  register: boolean;
  username: string;
  password: string;
  password2: string;
  messageToUser: string;
  messageLevel: string;

  constructor(private router: Router, private authService: AuthenticationService) { }

  ngOnInit() {
    this.register = false;
    this.messageLevel = MessageLevel.Default;
  }

  setRegister(register: boolean) {
    this.register = register;
  }

  resetPassword() {
    if (!this.username) {
      this.displayMessage("Enter username and try again.", MessageLevel.Default);
    }
    else {
      this.displayMessage(
        "Functionality not developed at this time.",
        MessageLevel.Warning);
      // FUTURE send email to user w/ link for reset (outside of scope)
      // this.displayMessage("Password reset link sent to email on file for "+this.username + ".",
      //   MessageLevel.Success);
    }
  }

  loginUser() {
    // TODO remove hardcoded dummy login and actually validate user login
    if (this.username === 'admin' && this.password === 'admin') {
      // this.router.navigate(['dashboard']);
      this.authService.login();
      this.router.navigate(['dashboard']);
    }
    else {
      this.displayMessage("Wrong username and/or password.", MessageLevel.Default);
    }
  }

  registerNewUser() {
    // FUTURE
    this.displayMessage("Functionality not developed at this time.",
      MessageLevel.Warning);
  }

  displayMessage(message: string, level: MessageLevel) {
    this.messageToUser = message;
    this.messageLevel = level.toString();
  }

  clearMessage() {
    this.messageToUser = null;
  }

  submit() {
    if (this.register) {
      this.registerNewUser();
    }
    else {
      this.loginUser();
    }
  }

}
