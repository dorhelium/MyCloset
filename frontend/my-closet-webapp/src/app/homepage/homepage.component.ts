import {Component, OnInit, ViewChild} from '@angular/core';
import * as $ from "jquery";
import {FormControl, FormGroup} from "@angular/forms";
import {User} from "../models/User.model";
import {ApiCommunicationService} from "../Service/api-communication.service";
import {AuthenticationService} from "../Service/authentication.service";
import {first} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";


class Alert {
  title: string | undefined;
  text: string | undefined;
}

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  currentUser: any;

  showRegisterForm = false;
  showLoginForm = false;

  checkEmail = false;

  userToRegister: User = new User({});

  userToLogin: User = new User({});

  loadingLayer = false;

  alert: Alert = new Alert();
  @ViewChild('content') modelContent: any;




  constructor(private apiCommunicationService: ApiCommunicationService,
              private authenticationService: AuthenticationService,
              private route: ActivatedRoute,
              private router: Router,
              private modalService: NgbModal) {
    this.authenticationService.currentUser.subscribe(user => this.currentUser = user);

  }

  ngOnInit(): void {

  }

  toggleMenu(){
    $('#offCanvasMenu').addClass('open');

    $('[data-dismiss="offCanvasMenu"]').click(function() {
      $(this).parent('#offCanvasMenu').removeClass('open');
    });
  }

  toggleSearch(){
    $('#searchModal').addClass('open');

    $('[data-dismiss="searchModal"]').click(function() {
      $(this).parent('#searchModal').removeClass('open');
    });
  }

  toggleRegister() {
    this.showRegisterForm = !this.showRegisterForm;
    if (!this.showRegisterForm){
      this.checkEmail = false;
      this.userToRegister = new User({});
    }
  }

  toggleLogin() {
    this.showLoginForm = !this.showLoginForm;
    if (!this.showLoginForm){
      this.userToLogin = new User({});
    }
  }

  registerUser() {
    this.loadingLayer = true;
    this.checkEmail = true;
    this.apiCommunicationService.registerUser(this.userToRegister).subscribe(
      res => {
        new User(res);
        this.alert.title = 'SUCCESS';
        this.alert.text = 'User '+ res.username+ ' is successfully registered!';
        this.loadingLayer = false;
        this.openVerticallyCentered(this.modelContent);
      },
      error=>{
        this.alert.title = 'WARNNING';
        this.alert.text =error.error.message;
        this.loadingLayer = false;
        this.openVerticallyCentered(this.modelContent);
      });

    this.userToRegister = new User({});
  }

  logout(){
    this.authenticationService.logout();
    this.router.navigate(['/homepage'])
  }


  loginUser(){
    if (this.userToLogin.email != null && this.userToLogin.password!=null) {

      this.authenticationService.login(
        this.userToLogin.email,
        this.userToLogin.email,
        this.userToLogin.password).pipe(first())
        .subscribe(res => {
          this.router.navigate(["/userpage"])
        },
          (error:any)=>{
          this.alert.title = 'WARNNING';
          this.alert.text =error.error.message;
          this.openVerticallyCentered(this.modelContent);
        });
    }
    this.userToLogin = new User({});
  }

  openVerticallyCentered(content: any) {

    this.modalService.open(content, { centered: true });
  }
}
