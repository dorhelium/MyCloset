import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../Service/authentication.service";
import * as $ from "jquery";
import {ApiCommunicationService} from "../Service/api-communication.service";
import {User} from "../models/User.model";
import {Product} from "../models/Product.model";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Item} from "../models/item.model";
import {Wishlist} from "../models/Wishlist";

class Alert {
  title: string | undefined;
  text: string | undefined;
}

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  currentUser: any;

  base64ToImage = 'data:image/jpeg;base64,';
  searchUrl: string = "";
  searchedProduct: Product | undefined;
  isLoading: boolean = false;


  itemToAdd: Item = new Item({});
  wishlist: Wishlist = new Wishlist({});

  alert: Alert = new Alert();
  @ViewChild('alertModel') modelContent: any;
  @ViewChild('content') searchModel: any;

  loadinglayer=false;

  constructor(private router: Router,
              private authenticationService: AuthenticationService,
              private apiCommunicationService: ApiCommunicationService,
              private modalService: NgbModal
              ){
    this.authenticationService.currentUser.subscribe(user => this.currentUser = user);
  }

  ngOnInit(): void {
    this.apiCommunicationService.getWishlist().subscribe(res=> {
      this.wishlist = new Wishlist(res);
      console.log(this.wishlist);
    });

  }

  logout(){
    this.authenticationService.logout();
    this.router.navigate(['/homepage'])
  }

  toggleMenu(){
    $('#offCanvasMenu').addClass('open');

    $('[data-dismiss="offCanvasMenu"]').click(function() {
      $(this).parent('#offCanvasMenu').removeClass('open');
    });
  }



  closeResult = '';

  open(content: any) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  getProduct(searchUrl: string) {
    this.isLoading = true;
    this.searchedProduct=undefined;
    this.apiCommunicationService.getProduct(searchUrl).subscribe(res=>{
      this.isLoading = false;
      this.searchedProduct = new Product(res);
      console.log(this.searchedProduct);
    }),
      ()=>{
        this.isLoading = false;
      }
  }


  updateColor(color: string) {
    this.itemToAdd.color=color;
  }
  updateSize(size: string) {
    this.itemToAdd.size=size;
  }

  addToWishlist() {
    this.loadinglayer = true;
    if (this.searchedProduct?.onSale){
      this.itemToAdd.addedPrice = this.searchedProduct.salePrice;
    }else{
      this.itemToAdd.addedPrice = this.searchedProduct?.originalPrice;
    }

    this.itemToAdd.product = this.searchedProduct;
    this.apiCommunicationService.addItemToWishlist(this.itemToAdd).subscribe(res=>{
      this.wishlist = new Wishlist(res);
      this.searchedProduct = undefined;
      this.itemToAdd = new Item({});
      this.searchUrl = "";
      this.loadinglayer = false;
    }),
      (error: any)=>{
        this.searchedProduct = undefined;
        this.itemToAdd = new Item({});
        this.searchUrl = "";
        this.alert.title = 'WARNNING';
        this.alert.text =error.error.message;
        this.loadinglayer = false;
        this.openVerticallyCentered(this.modelContent);
      }

  }

  addTracking(item: Item) {
    var itemToSend = new Item(item);
    itemToSend.tracking = !item.tracking;
    this.apiCommunicationService.addTracking(itemToSend).subscribe(res=>{
      var newItem = new Item(res);
     item.tracking = newItem.tracking;
    })

  }

  deleteItem(item: Item) {
    this.apiCommunicationService.deleteFromWishlist(item).subscribe(res=>{
      this.wishlist = new Wishlist(res);
    });
  }

  openVerticallyCentered(content: any) {
    this.modalService.open(content, { centered: true });
  }
}
