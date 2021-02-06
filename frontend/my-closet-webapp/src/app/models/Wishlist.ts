import {Item} from "./item.model";

export class Wishlist {
  id: number | undefined;
  items: Item[] | undefined;

  constructor(wishlist: any) {
    Object.assign(this, wishlist);
  }
}
