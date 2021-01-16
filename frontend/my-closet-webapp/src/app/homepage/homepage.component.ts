import { Component, OnInit } from '@angular/core';
import * as $ from "jquery";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  showRegisterForm=false;
  showLoginForm=false;

  constructor() { }

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
  }

  toggleLogin() {
    this.showLoginForm = !this.showLoginForm;
  }
}
