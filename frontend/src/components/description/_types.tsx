import { ColumnItem } from 'components/column/_types';
import { GetDescriptionPage } from 'generated/GetDescriptionPage';

export type DescriptionData = {
	id?: string;
	neighborsCol: ColumnItem[];
	childrenCol: ColumnItem[];
	title?: string;
	descriptions?: GetDescriptionPage['singleCategoryWithUrl']['descriptions'];
	loading?: boolean;
};
