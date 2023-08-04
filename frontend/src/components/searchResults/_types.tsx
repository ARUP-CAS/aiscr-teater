import { ColumnItem } from 'components/column/_types';

export type SearchResultsColumn = {
	title: string;
	data: ColumnItem[];
	key: string;
};

export type SearchResultsProps = {
	columns: SearchResultsColumn[];
	loading?: boolean;
};
