import {Wishlist} from "./Wishlist";

export class User {
  username: string | undefined;
  password: string | undefined;
  wishlist: Wishlist | undefined;
  email: string | undefined;

  constructor(user: any) {
    Object.assign(this, user);
  }
}
