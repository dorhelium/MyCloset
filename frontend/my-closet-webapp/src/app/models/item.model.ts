import {Product} from "./Product.model";

export class Item {
  id: number | undefined;
  product: Product | undefined;
  size: string | undefined;
  color: string | undefined;
  addedPrice: number | undefined;
  availableWhenAdded: boolean | undefined;
  tracking: boolean | undefined;

  constructor(item: any) {
    Object.assign(this, item);
  }
}
