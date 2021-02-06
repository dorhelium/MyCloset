import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../models/User.model";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Wishlist} from "../models/Wishlist";
import {Product} from "../models/Product.model";
import {Item} from "../models/item.model";


@Injectable({
  providedIn: 'root'
})
export class ApiCommunicationService {

  private readonly endPoint = 'http://localhost:8080'


  constructor(private http: HttpClient) { }

  registerUser(newUser: User): Observable<User> {
    const url = this.endPoint+`/register_user`;
    return this.http.post<User>(url, newUser);
  }

  getWishlistByUser (user: User): Observable<Wishlist> {
    const url = this.endPoint+`/user/` + user.username + `/wishlist`;
    return this.http.get<Wishlist>(url);
  }

  getProduct (searchUrl: any): Observable<Product> {
    const url = this.endPoint+`/search`;
    return this.http.post<Product>(url, searchUrl );
  }


  addItemToWishlist(itemToAdd: Item): Observable<Wishlist> {
    const url = this.endPoint+`/addItem`;
    return this.http.post<Wishlist>(url, itemToAdd );
  }

  getWishlist() {
    const url = this.endPoint+`/wishlist`;
    return this.http.get<Wishlist>(url);
  }

  addTracking(item: Item) {
    const url = this.endPoint+`/track`;
    return this.http.put<Wishlist>(url, item);
  }

  deleteFromWishlist(item: Item) {
    const url = this.endPoint+`/deleteItem`;
    return this.http.post<Wishlist>(url, item);
  }
}
